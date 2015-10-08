package com.intuit.idea.chopsticks.utils;

import org.jooq.lambda.tuple.Tuple2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Copyright 2015
 * Java to Java types
 *
 * @author albert
 */
public class CompareLookUp {
    static private Map<Tuple2<Class<?>, Class<?>>, BiFunction<Comparable, Comparable, Integer>> comp;

    static {
        comp = new HashMap<>();
        comp.put(new Tuple2<>(String.class, Integer.class), (a, b) -> ((String) a).compareTo(b.toString()));
    }

    public static BiFunction<Comparable, Comparable, Integer> findComparisonMethod(Class<?> sType, Class<?> tType) {
        if (comp.containsKey(new Tuple2<Class<?>, Class<?>>(sType, tType))) {
            return comp.get(new Tuple2<Class<?>, Class<?>>(sType, tType));
        } else if (comp.containsKey(new Tuple2<Class<?>, Class<?>>(tType, sType))) {
            BiFunction<Comparable, Comparable, Integer> tmp =
                    (t, v) -> comp.get(new Tuple2<Class<?>, Class<?>>(tType, sType)).apply(v, t);
            return tmp.andThen(i -> i * -1);
        } else {
            return (d, f) -> null;
        }
    }
}
