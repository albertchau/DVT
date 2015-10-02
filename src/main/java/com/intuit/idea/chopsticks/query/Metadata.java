package com.intuit.idea.chopsticks.query;

import java.util.function.BiFunction;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/18/15
 * ************************************
 */
public final class Metadata implements Comparable {
    private final String column;
    private final boolean pk;
    private final String sqlTypeName;
    private final Class<? extends Comparable> type;
    private final BiFunction<Comparable, Comparable, Integer> comparer;


    public Metadata(String column, boolean pk, String sqlTypeName, Class<? extends Comparable> type, BiFunction<Comparable, Comparable, Integer> comparer) {
        this.column = column;
        this.pk = pk;
        this.sqlTypeName = sqlTypeName;
        this.type = type;
        this.comparer = comparer;
    }

    public Metadata(String column, boolean pk, String sqlTypeName, Class<? extends Comparable> type) {
        this.column = column;
        this.pk = pk;
        this.sqlTypeName = sqlTypeName;
        this.type = type;
        this.comparer = null;
    }

    public static Metadata createWithType(String column, boolean pk, Class<? extends Comparable> type) {
        return new Metadata(column, pk, null, type, null);
    }

    public static Metadata createWithComparer(String column, boolean pk, BiFunction<Comparable, Comparable, Integer> comparer) {
        return new Metadata(column, pk, null, null, comparer);
    }

    public String getSqlTypeName() {
        return sqlTypeName;
    }

    public String getColumn() {
        return column;
    }

    public boolean isPk() {
        return pk;
    }

    public Class<? extends Comparable> getType() {
        return type;
    }

    public BiFunction<Comparable, Comparable, Integer> getComparer() {
        return comparer;
    }

    @Override
    public int compareTo(Object that) {
        if (!(that instanceof Metadata)) {
            throw new ClassCastException("Cannot compare Metadata.class to " + that.getClass() + ". Please only compare with Metadata classes");
        }
        if (this.isPk() && !((Metadata) that).isPk()) {
            return 1;
        }
        if (!this.isPk() && ((Metadata) that).isPk()) {
            return -1;
        }
        return column.compareToIgnoreCase(((Metadata) that).getColumn());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metadata)) {
            return false;
        }
        Metadata metadata = (Metadata) o;
        return isPk() == metadata.isPk() && getColumn().equalsIgnoreCase(metadata.getColumn()) && getType().equals(metadata.getType());

    }

    @Override
    public int hashCode() {
        int result = getColumn().hashCode();
        result = 31 * result + (isPk() ? 1 : 0);
        result = 31 * result + getType().hashCode();
        return result;
    }
}
