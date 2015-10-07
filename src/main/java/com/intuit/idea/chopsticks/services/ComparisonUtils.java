package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.utils.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.intuit.idea.chopsticks.services.CombinedMetadata.combineMetadata;
import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static java.util.Objects.isNull;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class ComparisonUtils {
    public static final Logger logger = LoggerFactory.getLogger(ComparisonUtils.class);

    @SuppressWarnings("unchecked")
    public static final BiFunction<Comparable, Comparable, Integer> comparer = Comparable::compareTo;

    /*
    Syntactic sugar for a functional lambda call to sort the list of comparable[]. Also a wrapper for overloaded compareColumns()
     */
    public static Integer compareColumns(Comparable[] source, Comparable[] target) {
        return compareColumns(source, target, filledBiFunctionArray(source.length), 0, source.length);
    }

    /*
    Just fills an array of Comparable.compareTo() BiFunctions to the size specified
     */
    @SuppressWarnings("unchecked")
    public static BiFunction<Comparable, Comparable, Integer>[] filledBiFunctionArray(int size) {
        BiFunction<Comparable, Comparable, Integer>[] comparers = new BiFunction[size];
        Arrays.fill(comparers, comparer);
        return comparers;
    }

    /*
    Syntactic sugar for functional lambda that retrieves a column value and casts the value to the type specified in the metadata
     */
    public static Function<Metadata, Comparable> getColumnValue(ResultSet rs) {
        return (md) -> {
            try {
                return md.getType().cast(rs.getObject(md.getColumn(), md.getType()));
            } catch (ClassCastException | SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    /*
    Compares the range of rows in array (specified by inclusive start and exclusive end
     */
    public static Integer compareColumns(Comparable[] sRow, Comparable[] tRow, BiFunction<Comparable, Comparable, Integer>[] comparers, int start, int end) {
        for (int i = start; i < end; i++) {
            int compareVal = comparers[i].apply(sRow[i], tRow[i]);
            if (compareVal != 0) {
                return compareVal;
            }
        }
        return 0;
    }

    /*todo
    Takes two metadata lists and finds the intersection
    1) Checks metadata length
    2) Sorts both metadata
    3) Makes a list of like metadata
    4) Throws an error if there are differing metadatas
    5) returns metadata
     */
    public static CombinedMetadata[] mergeMetadata(Metadata[] sMetadata, Metadata[] tMetadata) throws ComparisonException {
        if (sMetadata.length != tMetadata.length) {
            throw new ComparisonException("Metadata is not same length");
        }
        Arrays.sort(sMetadata);
        Arrays.sort(tMetadata);
        CombinedMetadata[] combinedMetadatas = IntStream.range(0, sMetadata.length)
                .boxed()
                .filter(i -> tMetadata[i].equals(sMetadata[i]))
                .map(i -> combineMetadata(sMetadata[i], ComparisonUtils.comparer, tMetadata[i]))
                .sorted()
                .toArray(CombinedMetadata[]::new);
        if (combinedMetadatas.length != sMetadata.length) {
            logger.error("Metadatas do not contain same content!");
//            throw new ComparisonException("Metadatas do not contain same content!");
        }
        return combinedMetadatas;
    }

    /*
    todo move to another util class because Data Provider uses this method too?
    From the result set (which contains data) find the metadata for them by using PK's as a guide.
    Important note: We get the column label not column name. Reason for this is that they are almost always the same,
    only differing when select column uses aliasing like "Select column_a AS col_a..." this will be useful for mappings...
    1) get the metadata for result set
    2) for each column, get the column label
    3) check to make sure column label is a Pk
    4) add all pk'd columns and return as Array
     */
    public static Metadata[] extractSpecifiedMetadata(ResultSet data, List<String> colsToBeExtracted, List<String> pks) throws SQLException {
        ResultSetMetaData metaData = data.getMetaData();
        return IntStream.range(1, metaData.getColumnCount() + 1).boxed()
                .map(i -> {
                    try {
                        String columnLabel = metaData.getColumnLabel(i);
                        Class<? extends Comparable> type = toClass(metaData.getColumnType(i));
                        boolean isPrimaryKey = (isNull(pks) ||
                                pks.isEmpty() ||
                                pks.stream()
                                        .anyMatch(pk -> pk.equalsIgnoreCase(columnLabel)));
                        boolean isToBeExtracted = isNull(colsToBeExtracted) ||
                                colsToBeExtracted.isEmpty() ||
                                colsToBeExtracted.stream()
                                        .anyMatch(col -> col.equalsIgnoreCase(columnLabel));
                        return isToBeExtracted ? new Metadata(columnLabel, isPrimaryKey, type) : null;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Metadata[]::new);
    }

    /*
    Wrapper for extractMetadata for times when PK is not specified in configuration
     */
    public static Metadata[] extractAllMetadata(ResultSet data) throws SQLException {
        return extractSpecifiedMetadata(data, null, null);
    }

    /**
     * Loads a result set into memory by iteratively adding columns based on the metadata specified. It then sorts all the rows.
     *
     * @param resultSet Iterator representing data to be fetched from Database
     * @param columns   Metadata that describes the columns such as column name and the java type to fetch.
     * @return A sorted list of comparable arrays. the elements in the list represent the rows and the elements in the array represents the columns
     * @throws SQLException
     */
    public static List<Comparable[]> resultSetToSortedList(ResultSet resultSet, CombinedMetadata[] columns, Function<CombinedMetadata, Metadata> whichOne) throws SQLException {
        List<Comparable[]> listOfRows = new ArrayList<>();
        while (resultSet.next()) {
            Comparable[] tmp = Arrays.stream(columns)
                    .map(whichOne)
                    .map(getColumnValue(resultSet))
                    .toArray(Comparable[]::new);
            listOfRows.add(tmp);
        }
        return listOfRows.stream()
                .sorted(ComparisonUtils::compareColumns)
                .collect(Collectors.toList());
    }

}
