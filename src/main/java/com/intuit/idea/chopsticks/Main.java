package com.intuit.idea.chopsticks;

import java.util.Arrays;
import java.util.List;

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