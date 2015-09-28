package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.query.Metadata;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.TransformerService;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
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
        List<Object[]> sRowList;
        try {
            sRowList = rowsToList(sData, columnNames);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving source's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
        }
        List<Object[]> tRowList;
        try {
            tRowList = rowsToList(tData, columnNames);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving target's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
        }
        comparisonStrategy(sRowList, tRowList, orderedPkMetadata);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    private void comparisonStrategy(List<Object[]> sRowList, List<Object[]> tRowList, final Metadata[] metadatas) {
        sortObjectList(metadatas, sRowList);
        sortObjectList(metadatas, tRowList);
        Iterator<Object[]> sRowIter = sRowList.iterator();
        Iterator<Object[]> tRowIter = tRowList.iterator();
        Object[] sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        Object[] tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        while (sRow != null && tRow != null) {
            try {
                Integer compareInt = checkIfComparableRows(metadatas, sRow, tRow);
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

    /**
     * Exposed for testing purposes. Used for when the result sets are column ordered and ready to be compared as is.
     * Will do a sort from left to right most columns before comparing the result sets.
     * Summary - treats every column like a primary key column with most significant on left.
     *
     * @param srcRs Source result set that is to be compared with target.
     * @param tarRs Target result set that is to be compared with source.
     */
    public void existenceCompare(ResultSet srcRs, ResultSet tarRs) {

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
        List<String[]> srcMetadataAsArrays;
        List<String[]> tarMetadataAsArrays;
        try {
            srcMetadataAsArrays = rowsToList(sMd, METADATA_COL_HEADERS);
            tarMetadataAsArrays = rowsToList(tMd, METADATA_COL_HEADERS);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Could not get metadata for the primary key columns.");
            throw new ComparisonException("Could not get metadata for the primary key columns.");
        }
        List<Metadata> srcMetadata = srcMetadataAsArrays.stream()
                .map(md -> new Metadata(md[0],
                        sortedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase(md[0])),
                        toClass(Integer.parseInt(md[1]))))
                .filter(Metadata::isPk)
                .sorted()
                .collect(Collectors.toList());
        List<Metadata> tarMetadata = tarMetadataAsArrays.stream()
                .map(md -> new Metadata(md[0],
                        sortedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase(md[0])),
                        toClass(Integer.parseInt(md[1]))))
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

    @SuppressWarnings("unchecked")
    private <T> List<T[]> rowsToList(ResultSet resultSet, List<String> columnNames) throws SQLException {
        List<T[]> listOfRows = new ArrayList<>();
        int rowWidth = columnNames.size();
        while (resultSet.next()) {
            T[] tmp = columnNames.stream()
                    .map(valueOfColumn(resultSet))
                    .toArray(size -> (T[]) new Object[rowWidth]);
            listOfRows.add(tmp);
        }
        return listOfRows;
    }

    private Function<String, Object> valueOfColumn(ResultSet rs) {
        return (columnName) -> {
            try {
                return rs.getObject(columnName);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }


    private void sortObjectList(Metadata[] columns,
                                List<Object[]> listOfRows) {
        listOfRows.sort((s, t) -> {
            try {
                return checkIfComparableRows(columns, s, t);
            } catch (ComparisonException e) {
                e.printStackTrace();
                logger.warn("During setup, error occurred when sorting rows.");
                return 0;
            }
        });
    }

    private Integer checkIfComparableRows(Metadata[] metadatas, Object[] sRow, Object[] tRow) throws ComparisonException {
        for (int i = 0; i < metadatas.length; i++) {
            if (!metadatas[i].isPk()) {
                break;
            }
            int compareVal = compareAs(sRow[i], tRow[i], metadatas[i].getType());
            if (compareVal != 0) {
                return compareVal;
            }
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    private int compareAs(Object src, Object tar, Class<? extends Comparable> type) throws ComparisonException {
        try {
            Comparable srcCast = TransformerService.convert(src, type);
            Comparable tarCast = TransformerService.convert(tar, type);
            return srcCast.compareTo(tarCast);
        } catch (Exception e) {
            logger.error("During comparison, Could not compare two types together: " + src.getClass() + " - to - " + tar.getClass() + ".");
            throw new ComparisonException("During comparison, Could not compare two types together: " + src.getClass() + " - to - " + tar.getClass() + ".");
        }
    }

    private void onlyFoundIn(final Object[] sRow, Metadata[] metadatas) {
        List<ColumnComparisonResult> tmp = IntStream.range(0, metadatas.length).boxed()
                .map(i -> createOnlySource(metadatas[i], sRow[i]))
                .collect(Collectors.toList());
        resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
    }

}
