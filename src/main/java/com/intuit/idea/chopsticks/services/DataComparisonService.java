package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.utils.Pair;
import com.intuit.idea.chopsticks.utils.TransformerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static com.intuit.idea.chopsticks.utils.adapters.ResultSetsAdapter.convert;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DataComparisonService implements ComparisonService {

    private static Logger logger = LoggerFactory.getLogger(DataComparisonService.class);

    private static Map<String, Class<? extends Comparable>> compareColumns(ResultSet sMd, ResultSet tMd) {
        Map<String, Class<? extends Comparable>> sMap = convert(sMd)
                .collect(Collectors
                        .toMap(t -> t.asString("COLUMN_NAME"), t -> toClass(t.asInt("DATA_TYPE"))));
        Map<String, Class<? extends Comparable>> tMap = convert(tMd)
                .collect(Collectors
                        .toMap(t -> t.asString("COLUMN_NAME"), t -> toClass(t.asInt("DATA_TYPE"))));

        if (sMap.equals(tMap)) {
            return sMap;
        } else {
            return null; //todo throw error
        }

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

    //TODO OOO Have dataprovider implement closable!!/autoclosable
    @Override
    public void compare(DataProvider source, DataProvider target) throws SQLException {
        source.openConnections();
        ResultSets sData = source.getData(this);
        target.openConnections();
        ResultSets tData = target.getData(this); //todo deal with random sampling

        List<String> pks = comparePrimaryKeys(source.getPrimaryKeys(), target.getPrimaryKeys());
        Map<String, Class<? extends Comparable>> columns = compareColumns(source.getMetadata(), target.getMetadata());
        long start = System.nanoTime();
        comparisonMethodStraight(sData, tData, pks, columns);
        long end = System.nanoTime();
        System.out.println("start - end /100000 = " + ((end - start) / 1000000));
        source.closeConnections();
        target.closeConnections();
    }

    private void comparisonMethodStraight(ResultSets sRs, ResultSets tRs, List<String> primaryKeys, Map<String, Class<? extends Comparable>> columnMap) throws SQLException {
        List<List<Object>> sListOfRows = new ArrayList<>();
        List<List<Object>> tListOfRows = new ArrayList<>();
        List<String> columnNames = new ArrayList<>(columnMap.keySet());
//todo prevent too large/signal list
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

        sortObjectList(primaryKeys, columnMap, sListOfRows);
        sortObjectList(primaryKeys, columnMap, tListOfRows);

        for (int i = 0; i < sListOfRows.size() && i < tListOfRows.size(); i++) {
            List<Object> objects1 = sListOfRows.get(i);
            List<Object> objects2 = tListOfRows.get(i);
            //todo finish merge compare
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < objects1.size(); j++) {
                Object val1 = objects1.get(j);
                Object val2 = objects2.get(j);
                if (val1.getClass().equals(val2.getClass())) {
                    val1 = TransformerService.convert(val1, val2.getClass());
                }

                sb.append(val1.equals(val2) ? "T" : "F");
            }
            System.out.println(objects1 + "<<<<<" + sb.toString() + ">>>>>" + objects2);
        }


    }

    private Function<String, Object> resultRowToList(ResultSets rs) {
        return (columnName) -> {
            try {
                return rs.getObject(columnName);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    private void sortObjectList(List<String> pks, Map<String, Class<? extends Comparable>> columns,
                                List<List<Object>> listOfRows) throws SQLException {
        List<Pair<Integer, Class<? extends Comparable>>> pkList = new ArrayList<>();

        // Does a mapping index of primary key columns that are stored in listOfRows vs what data getType those columns are
        int j = 0;
        for (Entry<String, Class<? extends Comparable>> entry : columns.entrySet()) {
            if (pks.contains(entry.getKey()))
                pkList.add(new Pair<>(j, entry.getValue()));
            j++;
        }

        listOfRows.sort((a, b) -> {
            return pkList.stream()
                    .mapToInt(pk -> compareAs(a.get(pk.getCar()), b.get(pk.getCar()), pk.getCdr()))
                    .filter(i -> i != 0)
                    .findFirst()
                    .orElse(0);
        });
    }

    private int compareAs(Object o, Object o1, Class<? extends Comparable> aClass) {
        //todo catch fatal exception from converting
        Comparable cast = TransformerService.convert(o, aClass);
        Comparable cast1 = TransformerService.convert(o1, aClass);
        return cast.compareTo(cast1);
    }

    @Override
    public void finish() {

    }
}
