//package com.intuit.idea.chopsticks.utils.functional;
//
//import java.sql.Connection;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
///**
// * Factory to produce tuple streams from result set
// */
//public class SQL {
//    public static Stream<Tuple> stream(final Connection connection, final String sql) {
//        ResultSetIterator resultSetIterator = new ResultSetIterator(connection, sql);
//        Iterable<Tuple> iterable = () -> resultSetIterator;
//        return StreamSupport.stream(iterable.spliterator(), false);
//    }
//}