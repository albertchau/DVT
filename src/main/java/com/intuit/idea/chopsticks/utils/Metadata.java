package com.intuit.idea.chopsticks.utils;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/18/15
 * ************************************
 */
public final class Metadata implements Comparable<Metadata> {
    private final String column;
    private final boolean pk;
    private final Class<? extends Comparable> type;


    public Metadata(String column, boolean pk, Class<? extends Comparable> type) {
        this.column = column;
        this.pk = pk;
        this.type = type;
    }

    public static Metadata createWithType(String column, boolean pk, Class<? extends Comparable> type) {
        return new Metadata(column, pk, type);
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


    @Override
    public int compareTo(Metadata that) {
        if (this.isPk() && !that.isPk()) {
            return -1;
        }
        if (!this.isPk() && that.isPk()) {
            return 1;
        }
        return column.compareToIgnoreCase(that.getColumn());
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
        return isPk() == metadata.isPk() && getColumn().equalsIgnoreCase(metadata.getColumn());

    }

    @Override
    public int hashCode() {
        int result = getColumn().hashCode();
        result = 31 * result + (isPk() ? 1 : 0);
        result = 31 * result + getType().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "column='" + column + '\'' +
                ", pk=" + pk +
                ", type=" + type +
                '}';
    }

}