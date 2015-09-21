package com.intuit.idea.chopsticks.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Tuples {

    @SuppressWarnings("unchecked")
    public static <T> Map<T, List<Tuple>> groupBy(Stream<Tuple> tupleStream, String column) {
        return tupleStream
                .collect(groupingBy((Tuple tuple) -> (T) tuple.val(column).get()));
    }

    public static <T> Map<T, List<Tuple>> groupBy(Collection<Tuple> tupleStream, String column) {
        return groupBy(tupleStream.stream(), column);

    }
}
