package com.intuit.idea.chopsticks.utils.containers;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/18/15
 * ************************************
 */
public final class Metadata implements Comparable<Metadata> {
    private final String columnLabel;
    private final boolean isPk;
    private final Class<? extends Comparable> type;
    private final String columnName;

    /**
     * Constructor for when there is no aliasing
     *
     * @param columnLabel The alias that is used as the actual column name
     * @param isPk        if this column is a primary key
     * @param type        what java type to process this column as
     */
    protected Metadata(String columnLabel, boolean isPk, Class<? extends Comparable> type) {
        this.columnLabel = columnLabel;
        this.columnName = columnLabel;
        this.isPk = isPk;
        this.type = type;
    }

    /**
     * Constructor for when there is aliasing
     *
     * @param columnLabel The alias that is used as the actual column name
     * @param isPk        if this column is a primary key
     * @param type        what java type to process this column as
     */
    protected Metadata(String columnLabel, String columnName, boolean isPk, Class<? extends Comparable> type) {
        this.columnLabel = columnLabel;
        this.columnName = columnName;
        this.isPk = isPk;
        this.type = type;
    }

    public static Metadata createWithNoAliasing(String columnLabel, boolean isPk, Class<? extends Comparable> type) {
        return new Metadata(columnLabel, isPk, type);
    }

    public static Metadata createWithAliasing(String columnLabel, String columnName, boolean isPk, Class<? extends Comparable> type) {
        return new Metadata(columnLabel, columnName, isPk, type);
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public String getColumnSelectStr() {
        return columnLabel.equalsIgnoreCase(columnName) ? columnLabel : columnName + " AS " + columnLabel;
    }

    public boolean isPk() {
        return isPk;
    }

    public Class<? extends Comparable> getType() {
        return type;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public int compareTo(Metadata that) {
        if (this.isPk() && !that.isPk()) {
            return -1;
        }
        if (!this.isPk() && that.isPk()) {
            return 1;
        }
        return columnLabel.compareToIgnoreCase(that.getColumnLabel());
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
        return isPk() == metadata.isPk() &&
                getColumnLabel().equalsIgnoreCase(metadata.getColumnLabel());
    }

    @Override
    public int hashCode() {
        int result = getColumnLabel().hashCode();
        result = 31 * result + (isPk() ? 1 : 0);
        result = 31 * result + getType().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "columnLabel='" + columnLabel + '\'' +
                ", isPk=" + isPk +
                ", type=" + type +
                '}';
    }
}
