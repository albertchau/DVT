package com.intuit.idea.chopsticks.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class SimpleResultSet extends AbstractResultSet implements ResultSet {

    private final SimpleResultSetMetaData simpleResultSetMetaData;
    List<List<Object>> items;
    private int current;

    public SimpleResultSet(List<List<Object>> items) throws SQLException {
        boolean isBadInput = isNull(items) || items.isEmpty() || isNull(items.get(0)) || items.get(0).isEmpty();
        if (isBadInput) {
            throw new SQLException("Bad input to simple result set.");
        }
        this.items = items;
        this.current = -1;
        List<Class<?>> types = items.get(0).stream()
                .map(Object::getClass)
                .collect(toList());
        simpleResultSetMetaData = new SimpleResultSetMetaData(types);
    }

    @Override
    public boolean next() {
        return ++current < items.size();
    }

    @Override
    public ResultSetMetaData getMetaData() {
        return simpleResultSetMetaData;
    }

    @Override
    public String getString(int col) {
        return curr().get(col - 1).toString();
    }

    private List<Object> curr() {
        return items.get(current);
    }

    @Override
    public int getInt(int col) {
        return (int) curr().get(col - 1);
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        Object o = curr().get(Integer.valueOf(columnLabel) - 1);
        return type.cast(o);
    }


}
