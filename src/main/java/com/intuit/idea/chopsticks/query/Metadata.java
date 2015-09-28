package com.intuit.idea.chopsticks.query;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/18/15
 * ************************************
 */
public class Metadata implements Comparable {
    private final String column;
    private final boolean pk;
    private final Class<? extends Comparable> type;

    public Metadata(String column, boolean pk, Class<? extends Comparable> type) {
        this.column = column;
        this.pk = pk;
        this.type = type;
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
