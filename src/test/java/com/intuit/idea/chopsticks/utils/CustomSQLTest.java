package com.intuit.idea.chopsticks.utils;

import com.intuit.idea.chopsticks.utils.functional.Tuple;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static com.intuit.idea.chopsticks.utils.functional.SQL.stream;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/13/15
 * ************************************
 */

public class CustomSQLTest {

    private Connection connection = null;
    private long startTime;
    private long endTime;

    @BeforeTest
    public void before() throws SQLException {
        //connect to DB
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "admin";
        connection = DriverManager.getConnection(url, user, password);
        startTime = System.nanoTime();
    }

    @Test
    public void customSQLTest() {
        String prim = "Name";
        String sql = "select " + prim + " from test.employees";
        List<String> pks = Collections.singletonList(prim);
        Map<String, Class<? extends Comparable>> mds = new HashMap<>();
        mds.put(prim, String.class);
        Iterator<Tuple> iterator1 = stream(connection, sql)
                .sorted((o1, o2) -> pks.stream()
                        .mapToInt(pk -> {
                            Comparable sVal = o1.val(pk, mds.get(pk)).get();
                            Comparable tVal = o2.val(pk, mds.get(pk)).get();
                            return sVal.compareTo(tVal);
                        })
                        .filter(i -> i != 0)
                        .findFirst()
                        .orElse(0))
                .collect(Collectors.toList())
                .iterator();
        Iterator<Tuple> iterator2 = stream(connection, sql)
                .sorted((o1, o2) -> pks.stream()
                        .mapToInt(pk -> {
                            Comparable sVal = o1.val(pk, mds.get(pk)).get();
                            Comparable tVal = o2.val(pk, mds.get(pk)).get();
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
            mds.forEach((s, aClass) -> {
                Comparable val1 = t1.val(s, aClass).get();
                Comparable val2 = t2.val(s, aClass).get();
                boolean equals = val1.equals(val2);
            });
            System.out.println(t1 + "<<<<<>>>>>" + t2);
        }
    }

    @AfterTest
    public void after() throws SQLException {
        endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        if (connection != null) {
            connection.close();
        }

        System.out.println(duration + "ms");
    }
}
