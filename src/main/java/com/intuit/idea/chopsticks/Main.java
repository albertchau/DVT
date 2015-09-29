package com.intuit.idea.chopsticks;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

enum EE {
    A(String.class, String.class, (String p, String q) -> q.compareTo(q));
//    S ((String p, String q) -> 1);
//    M ((p) -> (double) p.s*p.t),
//    D ((p) -> (double) p.s/p.t);
//
//    A ((p) -> (double) p.s+p.t),
//    S ((p) -> (double) p.s-p.t),
//    M ((p) -> (double) p.s*p.t),
//    D ((p) -> (double) p.s/p.t);


    private final BiFunction<? extends Comparable, ? extends Comparable, Integer> f;

    <T extends Comparable, U extends Comparable> EE(Class<T> s, Class<U> t, BiFunction<T, U, Integer> f) {
        this.f = f;
    }

    public BiFunction<? extends Comparable, ? extends Comparable, Integer> getF() {
        // for lookup... try a pair<t,u> with bifunction<t,u,integer>?
//        String.class > Integer.class ? 2 : 3; //http://stackoverflow.com/questions/29590333/store-a-list-of-unordered-pairs-in-java
        return f.andThen(i -> i + 2);
    }
}

/**
 * Copyright 2015
 *
 * @author albert
 */
public class Main {

    public static void main(String[] args) {
        List<String> sPks = Arrays.asList("A", "B", "C", "D");
        List<String> tPks = Arrays.asList("a", "b", "c");
        boolean ignoreCaseOut = sPks.stream().allMatch(sPk -> tPks.stream().anyMatch(tPk -> tPk.equalsIgnoreCase(sPk)));
        System.out.println("ignoreCaseOut = " + ignoreCaseOut);
        boolean caseSensitveOut = sPks.stream().allMatch(sPk -> tPks.stream().anyMatch(tPk -> tPk.equals(sPk)));
        System.out.println("caseSensitveOut = " + caseSensitveOut);
    }
}

class T2 {
    public Integer s;
    public Integer t;

    public T2(Integer s, Integer t) {
        this.s = s;
        this.t = t;
    }

    public static T2 t2(Integer s, Integer t) {
        return new T2(s, t);
    }
}