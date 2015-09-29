package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.query.Metadata;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createMatesFromMeta;
import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createOnlySource;
import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class ExistenceComparisonService implements ComparisonService {

    public static final List<String> METADATA_COL_HEADERS = Arrays.asList("COLUMN_NAME", "DATA_TYPE");
    private static final Logger logger = LoggerFactory.getLogger(ExistenceComparisonService.class);
    private Set<ResultStore> resultStores;

    public ExistenceComparisonService(Set<ResultStore> resultStores) {
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
    }

    @Override
    public void init() {

    }

    @Override
    public void compare(DataProvider source, DataProvider target) {

    }

    @Override
    public void finish() {

    }

    /**
     * Exposed for testing purposes. Used for when the result sets are not column ordered
     * and need some work done using defined metadata before comparing.
     * Will do a sort on significance using the primary key's lexigraphical ordering.
     *
     * @param sData Source result set that is to be compared with target.
     * @param tData Target result set that is to be compared with source.
     * @param sPks  Source primary key columns. Must exist in source result set columns. Order nor case matter.
     * @param tPks  Target primary key columns. Must exist in target result set columns. Order nor case matter.
     * @param sMd   ResultSet containing metadata that describes the srsRs. Namely, fields Specifically COLUMN_NAME and DATA_TYPE are looked at.
     * @param tMd   ResultSet containing metadata that describes the tarRs. Namely, fields Specifically COLUMN_NAME and DATA_TYPE are looked at.
     * @throws ComparisonException sdf
     */
    public void existenceCompare(ResultSet sData, ResultSet tData, List<String> sPks, List<String> tPks, ResultSet sMd, ResultSet tMd) throws ComparisonException {
        Metadata[] orderedPkMetadata = createMetadataForPrimaryKeys(sPks, sMd, tPks, tMd);
        long start = System.nanoTime();
        List<String> columnNames = Stream.of(orderedPkMetadata)
                .map(Metadata::getColumn)
                .collect(Collectors.toList());
        List<Comparable[]> sRowList;
        try {
            sRowList = rowsToList(sData, columnNames, Comparable.class);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving source's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
        }
        List<Comparable[]> tRowList;
        try {
            tRowList = rowsToList(tData, columnNames, Comparable.class);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving target's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
        }
        comparisonStrategy(sRowList, tRowList, orderedPkMetadata);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    /**
     * Exposed for testing purposes. Used for when the result sets are column ordered and ready to be compared as is.
     * Will do a sort from left to right most columns before comparing the result sets.
     * Summary - treats every column like a primary key column with most significant on left.
     *
     * @param sData Source result set that is to be compared with target.
     * @param tData Target result set that is to be compared with source.
     */
    public void existenceCompare(ResultSet sData, ResultSet tData) throws ComparisonException {
        long start = System.nanoTime();
        List<Comparable[]> sRowList;
        Metadata[] sMetadata;
        List<Comparable[]> tRowList;
        Metadata[] tMetadata;
        try {
            sMetadata = columnNamesFromResultSet(sData);
            List<String> columnNames = Stream.of(sMetadata)
                    .map(Metadata::getColumn)
                    .collect(Collectors.toList());
            sRowList = rowsToList(sData, columnNames, Comparable.class);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving source's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
        }
        try {
            tMetadata = columnNamesFromResultSet(tData);
            List<String> columnNames = Stream.of(tMetadata)
                    .map(Metadata::getColumn)
                    .collect(Collectors.toList());
            tRowList = rowsToList(tData, columnNames, Comparable.class);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving target's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
        }
        comparisonStrategy(sRowList, tRowList, sMetadata, tMetadata);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    private void comparisonStrategy(List<Comparable[]> sRowList, List<Comparable[]> tRowList, final Metadata[] metadatas) {
        sortObjectList(sRowList);
        sortObjectList(tRowList);
        Iterator<Comparable[]> sRowIter = sRowList.iterator();
        Iterator<Comparable[]> tRowIter = tRowList.iterator();
        Comparable[] sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        Comparable[] tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        while (sRow != null && tRow != null) {
            try {
                Integer compareInt = checkIfComparableRows(sRow, tRow);
                if (compareInt < 0) {
                    onlyFoundIn(sRow, metadatas);
                    sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                    logger.warn("Source row less than target. Incrementing source.");
                } else if (compareInt > 0) {
                    onlyFoundIn(tRow, metadatas);
                    tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                    logger.warn("Target row less than target. Incrementing target.");
                } else {
                    final Object[] finalSRow = sRow;
                    final Object[] finalTRow = tRow;
                    List<ColumnComparisonResult> tmp = IntStream.range(0, metadatas.length).boxed()
                            .map(i -> createMatesFromMeta(true, metadatas[i], finalSRow[i], finalTRow[i]))
                            .collect(Collectors.toList());
                    resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
                    sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                    tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                }
            } catch (ComparisonException e) {
                e.printStackTrace();
                logger.warn("Could not compare row: " + e.getMessage());
            }
        }
        while (sRow != null) {
            onlyFoundIn(sRow, metadatas);
            logger.info("There are rows only existing in source...");
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        }
        while (tRow != null) {
            onlyFoundIn(tRow, metadatas);
            logger.info("There are rows only existing in target...");
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }

    }

    private void comparisonStrategy(List<Comparable[]> sRowList, List<Comparable[]> tRowList, final Metadata[] sMetadata, final Metadata[] tMetadata) {
        sortObjectList(sRowList);
        sortObjectList(tRowList);
        Iterator<Comparable[]> sRowIter = sRowList.iterator();
        Iterator<Comparable[]> tRowIter = tRowList.iterator();
        Comparable[] sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        Comparable[] tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        while (sRow != null && tRow != null) {
            try {
                Integer compareInt = checkIfComparableRows(sRow, tRow);
                if (compareInt < 0) {
                    onlyFoundIn(sRow, sMetadata);
                    sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                    logger.warn("Source row less than target. Incrementing source.");
                } else if (compareInt > 0) {
                    onlyFoundIn(tRow, tMetadata);
                    tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                    logger.warn("Target row less than target. Incrementing target.");
                } else {
                    final Object[] finalSRow = sRow;
                    final Object[] finalTRow = tRow;
                    List<ColumnComparisonResult> tmp = IntStream.range(0, sMetadata.length).boxed()
                            .map(i -> createMatesFromMeta(true, sMetadata[i], finalSRow[i], finalTRow[i]))
                            .collect(Collectors.toList());
                    resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
                    sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                    tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                }
            } catch (ComparisonException e) {
                e.printStackTrace();
                logger.warn("Could not compare row: " + e.getMessage());
            }
        }
        while (sRow != null) {
            onlyFoundIn(sRow, sMetadata);
            logger.info("There are rows only existing in source...");
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        }
        while (tRow != null) {
            onlyFoundIn(tRow, tMetadata);
            logger.info("There are rows only existing in target...");
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }
    }

    private Metadata[] createMetadataForPrimaryKeys(List<String> sPks, ResultSet sMd, List<String> tPks, ResultSet tMd) throws ComparisonException {
        boolean allSrcPkExistInTar = sPks.stream()
                .allMatch(sPk -> tPks.stream()
                                .anyMatch(tPk -> tPk.equalsIgnoreCase(sPk))
                );
        boolean allTarPkExistInSrc = sPks.stream()
                .allMatch(sPk -> tPks.stream()
                                .anyMatch(tPk -> tPk.equalsIgnoreCase(sPk))
                );
        if (!allSrcPkExistInTar || !allTarPkExistInSrc) {
            logger.error("Primary keys don't match from the different result sets! Source Columns: "
                    + (sPks) + "; Target Columns: "
                    + (tPks) + ".");
            throw new ComparisonException("Primary keys don't match from the different result sets! Source Columns: "
                    + (sPks) + "; Target Columns: "
                    + (tPks) + ".");
        }
        List<String> sortedPks = sPks.stream()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
        List<Comparable[]> srcMetadataAsArrays;
        List<Comparable[]> tarMetadataAsArrays;
        try {
            srcMetadataAsArrays = rowsToList(sMd, METADATA_COL_HEADERS, Comparable.class);
            tarMetadataAsArrays = rowsToList(tMd, METADATA_COL_HEADERS, Comparable.class);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Could not get metadata for the primary key columns.");
            throw new ComparisonException("Could not get metadata for the primary key columns.");
        }
        List<Metadata> srcMetadata = srcMetadataAsArrays.stream()
                .map(md -> new Metadata((String) md[0],
                        sortedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase((String) md[0])),
                        toClass((Integer) md[1])))
                .filter(Metadata::isPk)
                .sorted()
                .collect(Collectors.toList());
        List<Metadata> tarMetadata = tarMetadataAsArrays.stream()
                .map(md -> new Metadata((String) md[0],
                        sortedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase((String) md[0])),
                        toClass((Integer) md[1])))
                .filter(Metadata::isPk)
                .sorted()
                .collect(Collectors.toList());
        List<Metadata> intersect = srcMetadata.stream()
                .filter(sCol -> tarMetadata.stream()
                        .anyMatch(tCol -> tCol.equals(sCol)))
                .sorted()
                .collect(Collectors.toList());
        if (intersect.size() != srcMetadata.size() || intersect.size() != tarMetadata.size()) {
            String sColsOnly = srcMetadata.stream()
                    .filter(sCol -> intersect.stream()
                            .noneMatch(in -> in.equals(sCol)))
                    .map(Metadata::getColumn)
                    .collect(Collectors.joining(", "));
            String tColsOnly = tarMetadata.stream()
                    .filter(tCol -> intersect.stream()
                            .noneMatch(in -> in.equals(tCol)))
                    .map(Metadata::getColumn)
                    .collect(Collectors.joining(", "));
            logger.error(String.format("Mismatched result set size after filtering by primary keys. Found %d columns in common. Found %d columns: %s in source. Found %d columns: %s in target.",
                    intersect.size(), srcMetadata.size(), sColsOnly, tarMetadata.size(), tColsOnly));
            throw new ComparisonException(String.format("Mismatched result set size after filtering by primary keys. Found %d columns in common. Found %d columns: %s in source. Found %d columns: %s in target.",
                    intersect.size(), srcMetadata.size(), sColsOnly, tarMetadata.size(), tColsOnly));
        }
        return intersect.stream().toArray(Metadata[]::new);
    }

    private Metadata[] columnNamesFromResultSet(ResultSet data) throws SQLException {
        ResultSetMetaData metaData = data.getMetaData();
        return IntStream.range(1, metaData.getColumnCount() + 1).boxed()
                .map(i -> {
                    try {
                        return new Metadata(metaData.getColumnName(i),
                                true,
                                toClass(metaData.getColumnType(i)));
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Metadata[]::new);
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable> List<T[]> rowsToList(ResultSet resultSet, List<String> columnNames, Class<T> type) throws SQLException {
        List<T[]> listOfRows = new ArrayList<>();
        while (resultSet.next()) {
            T[] tmp = columnNames.stream()
                    .map(valueOfColumn(resultSet, type))
                    .toArray(size -> (T[]) new Comparable[size]);
            listOfRows.add(tmp);
        }
        BiFunction<Metadata, Metadata, Integer> metadataObjectIntegerBiFunction = Metadata::compareTo;
        Comparator<String> stringComparator = String::compareTo;
        BiFunction<String, String, Integer> stringStringIntegerBiFunction = stringComparator::compare;
        stringStringIntegerBiFunction.apply("", "");
        return listOfRows;
    }

    private <T> Function<String, T> valueOfColumn(ResultSet rs, Class<T> type) {
        return (columnName) -> {
            try {
                return rs.getObject(columnName, type);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    private void sortObjectList(List<Comparable[]> listOfRows) {
        listOfRows.sort((s, t) -> {
            try {
                return checkIfComparableRows(s, t);
            } catch (ComparisonException e) {
                e.printStackTrace();
                logger.warn("During setup, error occurred when sorting rows.");
                return 0;
            }
        });
    }

    private Integer checkIfComparableRows(Comparable[] sRow, Comparable[] tRow) throws ComparisonException {
        for (int i = 0; i < sRow.length; i++) {
            int compareVal = sRow[i].compareTo(tRow[i]); //todo make sure they are comparable at beginning. SLA type thing
            if (compareVal != 0) {
                return compareVal;
            }
        }
        return 0;
    }

    private void onlyFoundIn(final Object[] sRow, Metadata[] metadatas) {
        List<ColumnComparisonResult> tmp = IntStream.range(0, metadatas.length).boxed()
                .map(i -> createOnlySource(metadatas[i], sRow[i]))
                .collect(Collectors.toList());
        resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
    }

}
