package com.intuit.idea.chopsticks.query;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/18/15
 * ************************************
 */
public class Metadata implements Comparable {
    private String column;
    private boolean pk;
    private Class<?> type;

    public Metadata(String column, boolean pk, Class<?> type) {
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

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Metadata))
            return -1;
        return column.compareTo(((Metadata) o).getColumn());
    }

    public Class<?> getType() {
        return type;
    }
}
