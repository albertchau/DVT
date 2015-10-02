package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.query.Metadata;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.TransformerService;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.*;
import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static com.intuit.idea.chopsticks.utils.adapters.ResultSetsAdapter.convert;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DataComparisonService implements ComparisonService {
    private static Logger logger = LoggerFactory.getLogger(DataComparisonService.class);
    private final Set<ResultStore> resultStores;

    public DataComparisonService(Set<ResultStore> resultStores) {
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
    }

    @Override
    public void init() {

    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        try {
            source.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not dataCompare connections to source.");
        }
        try {
            target.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not dataCompare connections to target.");
        }
        try (ResultSets sData = source.getData(this);
             ResultSets tData = target.getData(this)) { //todo deal with random sampling
            dataCompare(sData, tData, source.getPrimaryKeys(), target.getPrimaryKeys(), source.getMetadata(), target.getMetadata());
        } catch (DataProviderException | SQLException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
        source.closeConnections();
        target.closeConnections();
    }

    /**
     * Exposing this method so that we can do testing on it. Ultimately uses merge sort/compare to do comparison. Steps:
     * 1) Compares source/target metadata {@link #compareAndOrderColumns(ResultSet, ResultSet, List) metadata} and  {@link #compareAndOrderPrimaryKeys(List, List) primarykeys}
     * and creates an ordered master list of type {@link Metadata Metadata} that will determine which columns from the source and target result set to query
     * 2) Creates a List of Object[] from the sData using the metadata from step 1. This represents rows and its column contents. {@link #rowsToList(ResultSet, List) rowsToList}
     * 3) Creates a List of Object[] from the tData using the metadata from step 1. This represents rows and its column contents. {@link #rowsToList(ResultSet, List) rowsToList}
     * 4) Starts Merge Sort/Compare by sorting both lists based on primary keys. {@link #comparisonStrategy(List, List, Metadata[]) comparisonStrategy}
     * 5) Iterate through sourceList and targetList (step 2 and 3)
     * 6) Compare at each index only iterating one of the iterators if it cannot be paired and is less than its counterpart. Iterate both if can be compared.
     * 7) Report out results to {@link ResultStore resultStore}
     *
     * @param sData ResultSet of data to be compared as source against target
     * @param tData ResultSet of data to be compared as target against source
     * @param sPk   List of strings defining primary keys that exist in source
     * @param tPk   List of strings defining primary keys that exist in target
     * @param sMd   ResultSet that describes the source table. Specifically COLUMN_NAME and DATA_TYPE
     * @param tMd   ResultSet that describes the source target. Specifically COLUMN_NAME and DATA_TYPE
     * @throws ComparisonException Occurs when there is something wrong in test
     */
    public void dataCompare(ResultSet sData, ResultSet tData, List<String> sPk, List<String> tPk, ResultSet sMd, ResultSet tMd) throws ComparisonException {
        List<String> orderedPks = compareAndOrderPrimaryKeys(sPk, tPk);
        Metadata[] orderedMetadata = compareAndOrderColumns(sMd, tMd, orderedPks);
        long start = System.nanoTime();
        List<String> columnNames = Stream.of(orderedMetadata)
                .map(Metadata::getColumn)
                .collect(Collectors.toList());
        List<Object[]> sRowList = rowsToList(sData, columnNames);
        List<Object[]> tRowList = rowsToList(tData, columnNames);
        comparisonStrategy(sRowList, tRowList, orderedMetadata);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    private Metadata[] compareAndOrderColumns(ResultSet sMd, ResultSet tMd, List<String> orderedPks) throws ComparisonException {
        List<Metadata> sColumns = convert(sMd)
                .map(s -> new Metadata(s.asString("COLUMN_NAME"),
                        orderedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase(s.asString("COLUMN_NAME"))),
                        s.asString("TYPE_NAME"),
                        toClass(s.asInt("DATA_TYPE"))))
                .sorted()
                .collect(Collectors.toList());
        List<Metadata> tColumns = convert(tMd)
                .map(t -> new Metadata(t.asString("COLUMN_NAME"),
                        orderedPks.stream().anyMatch(pk -> pk.equalsIgnoreCase(t.asString("COLUMN_NAME"))),
                        t.asString("TYPE_NAME"),
                        toClass(t.asInt("DATA_TYPE"))))
                .sorted()
                .collect(Collectors.toList());
        List<Metadata> intersect = sColumns.stream()
                .filter(sCol -> tColumns.stream()
                        .anyMatch(tCol -> tCol.equals(sCol)))
                .collect(Collectors.toList());
        if (intersect.size() != sColumns.size() || intersect.size() != tColumns.size()) {
            String sColsOnly = sColumns.stream()
                    .filter(sCol -> intersect.stream()
                            .noneMatch(in -> in.equals(sCol)))
                    .map(Metadata::getColumn)
                    .collect(Collectors.joining(", "));
            String tColsOnly = tColumns.stream()
                    .filter(tCol -> intersect.stream()
                            .noneMatch(in -> in.equals(tCol)))
                    .map(Metadata::getColumn)
                    .collect(Collectors.joining(", "));
            logger.warn(String.format("Mismatched result set size. Found %d columns in common. Found %d columns: %s in source. Found %d columns: %s in target.",
                    intersect.size(), sColumns.size(), sColsOnly, tColumns.size(), tColsOnly));
        }
        long pksInIntersect = intersect.stream().filter(Metadata::isPk).count();
        if (pksInIntersect != orderedPks.size()) {
            logger.error("Some Primary Keys do not exist in the columns in our result set.");
            throw new ComparisonException("Some Primary Keys do not exist in the columns in our result set.");
        }
        return intersect.stream().toArray(Metadata[]::new);
    }

    private List<String> compareAndOrderPrimaryKeys(List<String> sPks, List<String> tPks) throws ComparisonException {
        List<String> masterPks = new ArrayList<>();
        for (String sPk : sPks) {
            if (tPks.stream().noneMatch(tPk -> tPk.equalsIgnoreCase(sPk))) {
                throw new ComparisonException("Primary Key lists do not match when comparing them.");
            }
            masterPks.add(sPk);
        }
        return masterPks.stream()
                .sorted(String::compareTo)
                .collect(Collectors.toList());
    }

    /*
     * todo prevent too large/signal list
     */
    private void comparisonStrategy(List<Object[]> sRowList,
                                    List<Object[]> tRowList,
                                    final Metadata[] metadatas) throws ComparisonException {
        final int rowWidth = metadatas.length;
        List<String> columnNames = Stream.of(metadatas)
                .map(Metadata::getColumn)
                .collect(Collectors.toList());
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
                    continue;
                }
                if (compareInt > 0) {
                    onlyFoundIn(tRow, metadatas);
                    tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                    logger.warn("Target row less than target. Incrementing target.");
                    continue;
                }
                List<ColumnComparisonResult> tmp = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < rowWidth; j++) {
                    if (metadatas[j].isPk()) {
                        tmp.add(createMatesFromMeta(true, metadatas[j], sRow[j], tRow[j]));
                        continue;
                    }
                    Object sVal = sRow[j];
                    Object tVal = tRow[j];
                    if (!sVal.getClass().equals(tVal.getClass())) {
                        sVal = TransformerService.convert(sVal, tVal.getClass());
                    }
                    boolean result = sVal.equals(tVal);
                    tmp.add(createMates(result, columnNames.get(j), columnNames.get(j), sVal, tVal, false));
                    sb.append(result ? "T" : "F");
                }
                resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
                logger.info(Arrays.toString(sRow) + "<<<<<" + sb.toString() + ">>>>>" + Arrays.toString(tRow));
                sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                tRow = tRowIter.hasNext() ? tRowIter.next() : null;
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

    private void onlyFoundIn(final Object[] sRow, Metadata[] metadatas) {
        List<ColumnComparisonResult> tmp = IntStream.range(0, metadatas.length).boxed()
                .map(i -> createOnlySource(metadatas[i], sRow[i]))
                .collect(Collectors.toList());
        resultStores.stream().forEach(rs -> rs.storeRowResults(this, tmp));
    }

    private List<Object[]> rowsToList(ResultSet resultSet, List<String> columnNames) throws ComparisonException {
        List<Object[]> listOfRows = new ArrayList<>();
        int rowWidth = columnNames.size();
        try {
            while (resultSet.next()) {
                Object[] tmp = columnNames.stream()
                        .map(valueOfColumn(resultSet))
                        .toArray(size -> new Object[rowWidth]);
                listOfRows.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving resultsets into memory failed.");
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

    @Override
    public void finish() {

    }
}
