package com.intuit.idea.ziplock.core.utils.containers;

import java.util.function.BiFunction;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class CombinedMetadata implements Comparable<CombinedMetadata> {
    private final String srcColumnLabel;
    private final Class<? extends Comparable> srcType;
    private final String tarColumnLabel;
    private final Class<? extends Comparable> tarType;
    private final BiFunction<Comparable, Comparable, Integer> comparer;
    private final boolean pk;
    private final Metadata tar;
    private final Metadata src;
    private final String srcColumnName;
    private final String tarColumnName;

    public CombinedMetadata(Metadata source, Metadata target, BiFunction<Comparable, Comparable, Integer> comparer) {
        this.src = source;
        this.tar = target;
        this.srcColumnLabel = source.getColumnLabel();
        this.srcType = source.getType();
        this.tarColumnLabel = target.getColumnLabel();
        this.tarType = target.getType();
        this.comparer = comparer;
        this.pk = source.isPk() && target.isPk();
        this.tarColumnName = target.getColumnName();
        this.srcColumnName = source.getColumnName();
    }

    public static CombinedMetadata combineMetadata(Metadata source, BiFunction<Comparable, Comparable, Integer> comparer, Metadata target) {
        return new CombinedMetadata(source, target, comparer);
    }

    public String getSrcColumnName() {
        return srcColumnName;
    }

    public String getTarColumnName() {
        return tarColumnName;
    }

    public Metadata getTar() {
        return tar;
    }

    public Metadata getSrc() {
        return src;
    }

    public String getSrcColumnLabel() {
        return srcColumnLabel;
    }

    public Class<? extends Comparable> getSrcType() {
        return srcType;
    }

    public String getTarColumnLabel() {
        return tarColumnLabel;
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
        return srcColumnLabel.compareToIgnoreCase(that.getSrcColumnLabel());
    }

}
