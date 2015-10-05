package com.intuit.idea.chopsticks;

import com.google.common.base.Supplier;
import com.intuit.idea.chopsticks.utils.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.DoubleSupplier;
import java.util.stream.IntStream;

/**
 * Copyright 2015
 * @link http://onoffswitch.net/simplifying-class-matching-java-8/
 * @author albert
 */
public class Main {

    static Map<Pair<Class<?>, Class<?>>, BiFunction> comp;

    static {
        comp = new HashMap<>();
        comp.put(new Pair<>(String.class, Integer.class), (a, b) -> a.equals(((Integer) (((Integer) b) + 4)).toString()));
    }

    public static void main(String[] args) {
        BiConsumer<String, String> xx = (a, b) -> System.out.println("a = " + a);
        BiConsumer<String, String> xxy = xx.andThen((a, b) -> System.out.println("b = " + b));

        BiFunction<String, String, Object> stringStringObjectBiFunction = (String a, String b) -> a == b;

        DoubleSupplier doubleSupplier = () -> 10;

        IntStream.range(1, 10).boxed().forEach(x -> print(() -> x));

    }

    private static void print(Supplier name) {
        name.get();
    }

}