package com.intuit.idea.chopsticks;

import org.jooq.lambda.tuple.Tuple2;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * Copyright 2015
 *
 * @author albert
 * @link http://onoffswitch.net/simplifying-class-matching-java-8/
 */
public class Main {

    static Map<Tuple2<Class<?>, Class<?>>, BiFunction> comp;

    static {
        comp = new HashMap<>();
        comp.put(new Tuple2<>(String.class, Integer.class), (a, b) -> a.equals(((Integer) (((Integer) b) + 4)).toString()));
    }

    public static void main(String[] args) {

        IntStream.range(1, 10)
                .boxed()
                .map(i -> i.toString())
                .forEach(System.out::println);

        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }

    }
}