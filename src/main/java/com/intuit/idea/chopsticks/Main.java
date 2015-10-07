package com.intuit.idea.chopsticks;

import com.intuit.idea.chopsticks.utils.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 * Copyright 2015
 *
 * @author albert
 * @link http://onoffswitch.net/simplifying-class-matching-java-8/
 */
public class Main {

    static Map<Pair<Class<?>, Class<?>>, BiFunction> comp;

    static {
        comp = new HashMap<>();
        comp.put(new Pair<>(String.class, Integer.class), (a, b) -> a.equals(((Integer) (((Integer) b) + 4)).toString()));
    }

    public static void main(String[] args) {

        Random rand = new Random();

        int bound = 10;
        String collect = IntStream.range(0, bound)/*.map(i -> rand.nextInt(bound * 10))*/
                .boxed()
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
        Collector<Integer, StringBuilder, String> b = Collector.of(
                () -> new StringBuilder(""),
                (s, t) -> s.append(t.toString()),
                StringBuilder::append,
                StringBuilder::toString);

        Collector<Integer, StringJoiner, String> a = Collector.of(
                () -> new StringJoiner(" | "),
                (s, t) -> s.add(t.toString()),
                StringJoiner::merge,
                StringJoiner::toString);
        String collect1 = IntStream.range(0, bound).map(i -> rand.nextInt(bound * 10))
                .boxed()
                .collect(a);
        System.out.println("collect1 = " + collect1);
        collect1 = IntStream.range(0, bound).map(i -> rand.nextInt(bound * 10))
                .boxed()
                .collect(b);
        System.out.println("collect1 = " + collect1);
        System.out.println(collect);
    }
}