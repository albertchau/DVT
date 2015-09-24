package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.utils.TransformerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static com.intuit.idea.chopsticks.utils.adapters.ResultSetsAdapter.convert;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DataComparisonService implements ComparisonService {

    private static Logger logger = LoggerFactory.getLogger(DataComparisonService.class);

    @SuppressWarnings("Convert2Diamond")
    private static List<Entry<String, Class<? extends Comparable>>> compareColumns(ResultSet sMd, ResultSet tMd) {
        List<Entry<String, Class<? extends Comparable>>> sColumns = convert(sMd)
                .map(t -> new SimpleEntry<String, Class<? extends Comparable>>(t.asString("COLUMN_NAME"), toClass(t.asInt("DATA_TYPE"))))
                .collect(Collectors.toList());
        List<Entry<String, Class<? extends Comparable>>> tColumns = convert(tMd)
                .map(t -> new SimpleEntry<String, Class<? extends Comparable>>(t.asString("COLUMN_NAME"), toClass(t.asInt("DATA_TYPE"))))
                .collect(Collectors.toList());

        List<Entry<String, Class<? extends Comparable>>> intersect = sColumns.stream()
                .filter(sCol -> tColumns.stream()
                        .filter(tCol -> tCol.getKey().equalsIgnoreCase(sCol.getKey()))
                        .findFirst()
                        .isPresent())
                .collect(Collectors.toList());
        if (intersect.size() != sColumns.size() || intersect.size() != tColumns.size()) {
            String sColsOnly = sColumns.stream()
                    .filter(sCol -> !intersect.stream()
                            .filter(in -> in.getKey().equalsIgnoreCase(sCol.getKey()))
                            .findAny()
                            .isPresent())
                    .map(Entry::getKey)
                    .collect(Collectors.joining(", "));
            String tColsOnly = tColumns.stream()
                    .filter(tCol -> !intersect.stream()
                            .filter(in -> in.getKey().equalsIgnoreCase(tCol.getKey()))
                            .findAny()
                            .isPresent())
                    .map(Entry::getKey)
                    .collect(Collectors.joining(", "));
            logger.warn(String.format("Mismatched result set size. Found %d columns in common. Found %d columns: %s in source. Found %d columns: %s in target.",
                    intersect.size(), sColumns.size(), sColsOnly, tColumns.size(), tColsOnly));
        }
        return intersect;

    }

    private static List<String> comparePrimaryKeys(List<String> sPk, List<String> tPk) {
        List<String> masterPks = new ArrayList<>();
        for (String s : sPk) {
            if (!tPk.contains(s)) {
                return null; //todo throw error
            }
            masterPks.add(s);
        }
        return masterPks;
    }

    @Override
    public void report(List<ColumnComparisonResult> rowResults) {

    }

    @Override
    public void init() {

    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws SQLException {
        source.openConnections();
        ResultSets sData = source.getData(this);
        target.openConnections();
        ResultSets tData = target.getData(this); //todo deal with random sampling

        open(sData, tData, source.getPrimaryKeys(), target.getPrimaryKeys(), source.getMetadata(), target.getMetadata());

        source.closeConnections();
        target.closeConnections();
    }

    public void open(ResultSet sData, ResultSet tData, List<String> sPk, List<String> tPk, ResultSet sMd, ResultSet tMd) throws SQLException {
        List<String> pks = comparePrimaryKeys(sPk, tPk);
        List<String> alphabetizedPks = pks.stream().sorted(String::compareTo).collect(Collectors.toList());
        List<Entry<String, Class<? extends Comparable>>> unorderedColumns = compareColumns(sMd, tMd);
        List<Entry<String, Class<? extends Comparable>>> orderedColumns = sortAndOrderColumns(alphabetizedPks, unorderedColumns);
        long start = System.nanoTime();
        comparisonMethodStraight(sData, tData, alphabetizedPks, orderedColumns);
        long end = System.nanoTime();
        System.out.println("start - end /100000 = " + ((end - start) / 1000000));
    }

    /**
     * @param alphabetizedPks  primary keys that are alphabetized prior to passing in
     * @param unorderedColumns columns that are unordered that have the primary keys.
     * @return columns that are sorted alphabetically with the primary keys at the beginning of the list which are also sorted
     */
    private List<Entry<String, Class<? extends Comparable>>> sortAndOrderColumns(List<String> alphabetizedPks,
                                                                                 List<Entry<String, Class<? extends Comparable>>> unorderedColumns) {
        List<Entry<String, Class<? extends Comparable>>> alphabetizedPkColumns = alphabetizedPks.stream()
                .map(pk -> unorderedColumns.stream()
                        .filter(col -> col.getKey().equalsIgnoreCase(pk))
                        .findFirst()
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (alphabetizedPkColumns.size() != alphabetizedPks.size()) {
            logger.error("Some Primary Keys do not exist in the columns in our result set.");
            return null; // todo throw error
        }
        Stream<Entry<String, Class<? extends Comparable>>> sortedColumnsWithoutPks = unorderedColumns.stream()
                .filter(e -> !alphabetizedPks.stream()
                        .filter(pk -> pk.equalsIgnoreCase(e.getKey()))
                        .findAny()
                        .isPresent())
                .sorted((a, b) -> a.getKey().compareTo(b.getKey()));
        return Stream.concat(alphabetizedPkColumns.stream(), sortedColumnsWithoutPks).collect(Collectors.toList());
    }

    /**
     * @param sRs         Source Result Set
     * @param tRs         Target Result Set
     * @param primaryKeys Primary Keys whose columns exist both in source and target result sets. Also Alphabetized.
     * @param columns     Columns that are sorted first my sorted primary keys and then by column names
     * @throws SQLException
     */
    private void comparisonMethodStraight(ResultSet sRs,
                                          ResultSet tRs,
                                          final List<String> primaryKeys,
                                          final List<Map.Entry<String, Class<? extends Comparable>>> columns) throws SQLException {
        List<List<Object>> sListOfRows = new ArrayList<>();
        List<List<Object>> tListOfRows = new ArrayList<>();
        List<String> columnNames = new ArrayList<>(columns.stream()
                .map(Entry::getKey)
                .collect(Collectors.toList()));
        // todo prevent too large/signal list
        while (sRs.next()) {
            List<Object> tmp = columnNames.stream()
                    .map(resultRowToList(sRs))
                    .collect(Collectors.toList());
            sListOfRows.add(tmp);
        }
        while (tRs.next()) {
            List<Object> tmp = columnNames.stream()
                    .map(resultRowToList(tRs))
                    .collect(Collectors.toList());
            tListOfRows.add(tmp);
        }
        sortObjectList(primaryKeys, columns, sListOfRows);
        sortObjectList(primaryKeys, columns, tListOfRows);
        Iterator<List<Object>> sRowIter = sListOfRows.iterator();
        Iterator<List<Object>> tRowIter = tListOfRows.iterator();
        List<Object> sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        List<Object> tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        while (sRow != null && tRow != null) {
            Integer compareInt = checkIfComparableRows(primaryKeys, columns, sRow, tRow);
            if (compareInt < 0) {
                sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                logger.warn("Source row less than target. Incrementing source.");
                continue;
            }
            if (compareInt > 0) {
                tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                logger.warn("Target row less than target. Incrementing target.");
                continue;
            }
            StringBuilder sb = new StringBuilder();
            for (int j = primaryKeys.size(); j < sRow.size(); j++) {
                Object val1 = sRow.get(j);
                Object val2 = tRow.get(j);
                if (!val1.getClass().equals(val2.getClass())) {
                    val1 = TransformerService.convert(val1, val2.getClass());
                }
                sb.append(val1.equals(val2) ? "T" : "F");
            }
            logger.info(sRow + "<<<<<" + sb.toString() + ">>>>>" + tRow);
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }
        while (sRow != null) {
            logger.info("There are rows only existing in source...");
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        }
        while (tRow != null) {
            logger.info("There are rows only existing in target...");
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }
    }

    private Integer checkIfComparableRows(List<String> primaryKeys, List<Entry<String, Class<? extends Comparable>>> columns, List<Object> sRow, List<Object> tRow) {
        for (int i = 0; i < primaryKeys.size(); i++) {
            int compareVal = compareAs(sRow.get(i), tRow.get(i), columns.get(i).getValue());
            if (compareVal != 0) {
                return compareVal;
            }
        }
        return 0;
    }

    private Function<String, Object> resultRowToList(ResultSet rs) {
        return (columnName) -> {
            try {
                return rs.getObject(columnName);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    private void sortObjectList(List<String> pks, List<Entry<String, Class<? extends Comparable>>> columns,
                                List<List<Object>> listOfRows) throws SQLException {
        listOfRows.sort((s, t) -> checkIfComparableRows(pks, columns, s, t));
    }

    @SuppressWarnings("unchecked")
    private int compareAs(Object src, Object tar, Class<? extends Comparable> type) {
        //todo catch fatal exception from converting
        Comparable srcCast = TransformerService.convert(src, type);
        Comparable tarCast = TransformerService.convert(tar, type);
        return srcCast.compareTo(tarCast);
    }

    @Override
    public void finish() {

    }
}
