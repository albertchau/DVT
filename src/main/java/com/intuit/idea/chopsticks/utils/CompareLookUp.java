package com.intuit.idea.chopsticks.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Copyright 2015
 * Java to Java types
 * @author albert
 */
public class CompareLookUp {
    static private Map<Pair<Class<?>, Class<?>>, BiFunction<Comparable, Comparable, Integer>> comp;
    static {
        comp = new HashMap<>();
        comp.put(new Pair<>(String.class, Integer.class), (a, b) -> ((String) a).compareTo(b.toString()));
    }

    public static BiFunction<Comparable, Comparable, Integer> findComparisonMethod(Class<?> sType, Class<?> tType) {
        if (comp.containsKey(new Pair<Class<?>, Class<?>>(sType, tType))) {
            return comp.get(new Pair<Class<?>, Class<?>>(sType, tType));
        } else if (comp.containsKey(new Pair<Class<?>, Class<?>>(tType, sType))) {
            BiFunction<Comparable, Comparable, Integer> tmp =
                    (t, v) -> comp.get(new Pair<Class<?>, Class<?>>(tType, sType)).apply(v, t);
            return tmp.andThen(i -> i * -1);
        } else {
            return (d, f) -> null;
        }
    }
}
