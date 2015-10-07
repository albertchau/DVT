package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.utils.Metadata;

import java.util.function.BiFunction;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class CombinedMetadata implements Comparable<CombinedMetadata> {
    private final String srcColumn;
    private final Class<? extends Comparable> srcType;
    private final String tarColumn;
    private final Class<? extends Comparable> tarType;
    private final BiFunction<Comparable, Comparable, Integer> comparer;
    private final boolean pk;
    private final Metadata tar;
    private final Metadata src;

    public CombinedMetadata(Metadata source, Metadata target, BiFunction<Comparable, Comparable, Integer> comparer) {
        this.src = source;
        this.tar = target;
        this.srcColumn = source.getColumn();
        this.srcType = source.getType();
        this.tarColumn = target.getColumn();
        this.tarType = target.getType();
        this.comparer = comparer;
        this.pk = source.isPk() && target.isPk();
    }

    public static CombinedMetadata combineMetadata(Metadata source, BiFunction<Comparable, Comparable, Integer> comparer, Metadata target) {
        return new CombinedMetadata(source, target, comparer);
    }

    public Metadata getTar() {
        return tar;
    }

    public Metadata getSrc() {
        return src;
    }

    public String getSrcColumn() {
        return srcColumn;
    }

    public Class<? extends Comparable> getSrcType() {
        return srcType;
    }

    public String getTarColumn() {
        return tarColumn;
    }

    public Class<? extends Comparable> getTarType() {
        return tarType;
    }

    public BiFunction<Comparable, Comparable, Integer> getComparer() {
        return comparer;
    }

    public boolean isPk() {
        return pk;
    }

    @Override
    public int compareTo(CombinedMetadata that) {
        if (this.isPk() && !that.isPk()) {
            return -1;
        }
        if (!this.isPk() && that.isPk()) {
            return 1;
        }
        return srcColumn.compareToIgnoreCase(that.getSrcColumn());
    }
}
