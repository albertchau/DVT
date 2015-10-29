package com.intuit.idea.chopsticks.utils;

import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static com.intuit.idea.chopsticks.utils.containers.CombinedMetadata.combineMetadata;
import static com.intuit.idea.chopsticks.utils.containers.Metadata.createWithNoAliasing;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;


public class ComparisonUtils {
    public static final Logger logger = LoggerFactory.getLogger(ComparisonUtils.class);

    /**
     * The default comparer that handles comparing nulls.
     */
    @SuppressWarnings("unchecked")
    public static final BiFunction<Comparable, Comparable, Integer> nullSafeComparer = (a, b) -> {
        if (a == null ^ b == null) {
            return (a == null) ? -1 : 1;
        }
        if (a == null) {
            return 0;
        }
        return a.compareTo(b);
    };

    /**
     * Syntactic sugar for a functional lambda call to sort the list of comparable[] that uses every column in it's comparison. Also a wrapper for overloaded compareColumns().
     *
     * @param source a comparable array
     * @param target a comparable array
     * @return 0 if equal, 1 if source is greater -1 if target is greater
     */
    public static Integer compareColumns(Comparable[] source, Comparable[] target) {
        return compareColumns(source, target, filledBiFunctionArray(source.length), 0, source.length);
    }

    /**
     * Just fills an array of Comparable.compareTo() BiFunctions to the size specified.
     *
     * @param size Specifies how big the array will be.
     * @return Array of nullSafeComparers
     */
    @SuppressWarnings("unchecked")
    public static BiFunction<Comparable, Comparable, Integer>[] filledBiFunctionArray(int size) {
        BiFunction<Comparable, Comparable, Integer>[] comparers = new BiFunction[size];
        Arrays.fill(comparers, nullSafeComparer);
        return comparers;
    }

    /**
     * Syntactic sugar for functional lambda that retrieves a column value and casts the value to the type specified in the metadata.
     *
     * @param rs Result Set to get column value from.
     * @return a Function that takes in Metadata and returns a comparable value.
     */
    public static Function<Metadata, Comparable> getColumnValue(ResultSet rs) {
        return (md) -> {
            try {
                return md.getType().cast(rs.getObject(md.getColumnLabel(), md.getType()));
            } catch (ClassCastException | SQLException e) {
                logger.error("Could not get column value because: " + e.getMessage());
                return null;
            }
        };
    }

    /**
     * Compares the range of rows in array (specified by inclusive start and exclusive end.
     *
     * @param sRow      Array of comparables.
     * @param tRow      Array of comparables.
     * @param comparers Array of functions that compare the respective sRow[start...end] to tRow[start...end].
     * @param start     Inclusive where to start comparing the columns of the rows.
     * @param end       Exclusive of where to stop comparing the columns of the rows.
     * @return 0 if equal, 1 if source is greater -1 if target is greater
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

    /**
     * Takes two metadata lists and finds the intersection
     * 1) Checks metadata length
     * 2) Sorts both metadata
     * 3) Makes a list of like metadata
     * 4) Throws an error if there are differing metadatas
     * 5) returns metadata
     *
     * @param sMetadata List of metadata
     * @param tMetadata List of metadata
     * @return an Array of combined metadata
     * @throws ComparisonException
     */
    public static CombinedMetadata[] mergeMetadata(List<Metadata> sMetadata, List<Metadata> tMetadata) throws ComparisonException {
        if (sMetadata.size() != tMetadata.size()) {
            throw new ComparisonException("Metadata is not same length");
        }
        Collections.sort(sMetadata);
        Collections.sort(tMetadata);
        CombinedMetadata[] combinedMetadatas = IntStream.range(0, sMetadata.size())
                .boxed()
                .filter(i -> tMetadata.get(i).equals(sMetadata.get(i)))
                .map(i -> combineMetadata(sMetadata.get(i), ComparisonUtils.nullSafeComparer, tMetadata.get(i)))
                .sorted()
                .toArray(CombinedMetadata[]::new);
        if (combinedMetadatas.length != sMetadata.size()) {
            logger.error("Metadatas do not contain same content!");
        }
        return combinedMetadatas;
    }

    /**
     * From the result set (which contains data) find the metadata for them by using PK's as a guide.
     * Important note: We get the column label not column name. Reason for this is that they are almost always the same,
     * only differing when select column uses aliasing like "Select column_a AS col_a..." this will be useful for mappings...
     * 1) get the metadata for result set
     * 2) for each column, get the column label
     * 3) check to make sure column label is a Pk
     * 4) add all pk'd columns and return as Array
     *
     * @param data              Result Set of data.
     * @param colsToBeExtracted List of Strings specifying which strings to be extracted.
     * @param pks               List of strings specifying which primary keys to be extracted.
     * @return List of metadata.
     * @throws SQLException
     */
    public static List<Metadata> extractSpecifiedMetadata(ResultSet data, List<String> colsToBeExtracted, List<String> pks) throws SQLException {
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
                        return isToBeExtracted ? createWithNoAliasing(columnLabel, isPrimaryKey, type) : null;
                    } catch (SQLException e) {
                        logger.error("Could not extract metadata: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toList());
    }

    /**
     * Find the data that exists in left but not in right... SQL is Select * from `left` Left Outer Join `right` on `left`._ = `right`._ where `right`._ = null.
     *
     * @param left    List of data of type T.
     * @param right   List of data of type T.
     * @param equalTo A BiPredicate function to compare types of Left and types of Right.
     * @param <T>     Any Type
     * @return List of data of Type T.
     */
    public static <T> List<T> findLeftNotInRight(List<T> left, List<T> right, BiPredicate<T, T> equalTo) {
        return left.stream()
                .filter(l -> right.stream()
                        .noneMatch(r -> equalTo.test(r, l)))
                .collect(toList());
    }

    /**
     * Wrapper for extractMetadata for times when PK is not specified in configuration.
     *
     * @param data A Result Set.
     * @return List of Metadata
     * @throws SQLException
     */
    public static List<Metadata> extractAllMetadata(ResultSet data) throws SQLException {
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
                .collect(toList());
    }

}
