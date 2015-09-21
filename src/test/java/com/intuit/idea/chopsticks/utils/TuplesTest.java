package com.intuit.idea.chopsticks.utils;


import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import static com.intuit.idea.chopsticks.utils.SQL.stream;
import static com.intuit.idea.chopsticks.utils.Tuples.groupBy;
import static java.util.stream.Collectors.toList;
import static org.testng.AssertJUnit.assertTrue;

public class TuplesTest {

    @Test
    public void testGroupBy() throws Exception {
        Connection connection = DriverManager.getConnection("");

        //test the basic streaming
        List<? super Integer> persons = stream(connection, "select distinct(person_id) from testing.persons").map((tuple) -> tuple.asInt("testing.persons.person_id")).collect(toList());
        Map<Integer, List<Tuple>> result = groupBy(stream(connection, ""), "testing.persons.person_id");
        System.out.println(result.keySet());
        result.keySet().forEach((key) -> assertTrue(persons.contains(key)));

        //collection streaming
        List<Tuple> tuples = result.get(1);
        List<Tuple> reduced = tuples.stream().map((tuple) -> tuple.reduce("testing.orders.")).collect(toList());
        Map<Object, List<Tuple>> orders = groupBy(reduced, "testing.orders.order_id");
        System.out.println(orders.keySet());
    }
}
