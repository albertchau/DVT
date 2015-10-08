package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.utils.functional.Tuple;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static com.intuit.idea.chopsticks.utils.adapters.ResultSetsAdapter.convert;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/17/15
 * ************************************
 */
public class TupleComparison {

    private static Map<String, Class<? extends Comparable>> compareColumns(ResultSetMetaData sMd, ResultSetMetaData tMd) throws SQLException {
        int sColCount = sMd.getColumnCount();
        int tColCount = tMd.getColumnCount();
        //first check... column count is the same
        if (sColCount != tColCount) {
//            logger.error("cannot continue comparison because the number of columns from target is different. Source: " + sColCount + ". Target: " + tColCount + ".");
            throw new IllegalArgumentException("cannot continue comparison because the number of columns from target is different. Source: " + sColCount + ". Target: " + tColCount + ".");
        }

        Map<String, Class<? extends Comparable>> sMap = new HashMap<>();
        Map<String, Class<? extends Comparable>> tMap = new HashMap<>();
        Map<String, Class<? extends Comparable>> masterMap = new HashMap<>();

        for (int i = 1; i < sColCount + 1; i++) {
            String field = sMd.getColumnName(i);
//            Class<?> getType = Class.forName(sMd.getColumnClassName(i)); todo: research more
            Class<? extends Comparable> type = toClass(sMd.getColumnType(i));
            sMap.put(field, type);
        }
        for (int i = 1; i < tColCount + 1; i++) {
            String field = tMd.getColumnName(i);
//            Class<?> getType = Class.forName(tMd.getColumnClassName(i)); todo: research more
            Class<? extends Comparable> type = toClass(tMd.getColumnType(i));
            tMap.put(field, type);
        }

        //second check... check metadatas
        for (Map.Entry<String, Class<? extends Comparable>> sourceColumnInfo : sMap.entrySet()) {
            String field = sourceColumnInfo.getKey();
            Class<? extends Comparable> type = sourceColumnInfo.getValue();

            if (type == null) {
                if (!(tMap.get(field) == null && tMap.containsKey(field)))
                    return null; //todo throw error
            } else {
                if (!type.equals(tMap.get(field)))
                    return null; //todo throw error
            }
            //if they match (would have returned null otherwise) then put into master map
            masterMap.put(field, type);
        }

        return masterMap;
    }

    private void comparisonMethodWithTuples(ResultSets sData, ResultSets tData, List<String> pks, Map<String, Class<? extends Comparable>> columns) {
        Iterator<Tuple> iterator1 = convert(sData)
                .sorted((o1, o2) -> pks.stream()
                        .mapToInt(pk -> {
                            Comparable sVal = o1.val(pk, columns.get(pk)).get();
                            Comparable tVal = o2.val(pk, columns.get(pk)).get();
                            return sVal.compareTo(tVal);
                        })
                        .filter(i -> i != 0)
                        .findFirst()
                        .orElse(0))
                .collect(Collectors.toList())
                .iterator();

        Iterator<Tuple> iterator2 = convert(tData)
                .sorted((o1, o2) -> pks.stream()
                        .mapToInt(pk -> {
                            Comparable sVal = o1.val(pk, columns.get(pk)).get();
                            Comparable tVal = o2.val(pk, columns.get(pk)).get();
                            return sVal.compareTo(tVal);
                        })
                        .filter(i -> i != 0)
                        .findFirst()
                        .orElse(0))
                .collect(Collectors.toList())
                .iterator();


        while (iterator1.hasNext() && iterator2.hasNext()) {
            Tuple t1 = iterator1.next();
            Tuple t2 = iterator2.next();

            StringBuilder sb = new StringBuilder();
            columns.forEach((s, aClass) -> {
                Comparable val1 = t1.val(s, aClass).get();
                Comparable val2 = t2.val(s, aClass).get();
                sb.append(val1.equals(val2) ? "T" : "F");
            });
            System.out.println(t1 + "<<<<<" + sb.toString() + ">>>>>" + t2);
        }
    }

    //todo probably need to do some lookup/comparison table thing
    private Class<? extends Comparable> convertToComparable(Class<?> type) {
//        Class<? extends Comparable> y;
//
//        getType.cast(Class<?>)
//
//        Class<? extends Comparable> x = (Class<? extends Comparable>) getType;
        return String.class;
    }

}
