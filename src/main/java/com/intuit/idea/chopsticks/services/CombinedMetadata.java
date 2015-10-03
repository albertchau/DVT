package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.query.Metadata;

import java.util.function.BiFunction;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class CombinedMetadata {
    private final String srcColumn;
    private final Class<? extends Comparable> srcType;
    private final String targetColumn;
    private final Class<? extends Comparable> tarType;
    private final BiFunction<Comparable, Comparable, Integer> comparer;

    public CombinedMetadata(String srcColumn, Class<? extends Comparable> srcType, String targetColumn, Class<? extends Comparable> tarType, BiFunction<Comparable, Comparable, Integer> comparer) {
        this.srcColumn = srcColumn;
        this.srcType = srcType;
        this.targetColumn = targetColumn;
        this.tarType = tarType;
        this.comparer = comparer;
    }

    public static CombinedMetadata combineMetadata(Metadata source, BiFunction<Comparable, Comparable, Integer> comparer, Metadata target) {
        return new CombinedMetadata(source.getColumn(), source.getType(), target.getColumn(), target.getType(), comparer);
    }

    public String getSrcColumn() {
        return srcColumn;
    }

    public Class<? extends Comparable> getSrcType() {
        return srcType;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public Class<? extends Comparable> getTarType() {
        return tarType;
    }

    public BiFunction<Comparable, Comparable, Integer> getComparer() {
        return comparer;
    }
}
