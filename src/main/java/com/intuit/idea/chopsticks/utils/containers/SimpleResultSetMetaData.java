package com.intuit.idea.chopsticks.utils.containers;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class SimpleResultSetMetaData extends AbstractResultSetMetaData implements ResultSetMetaData {
    private final List<Class<?>> types;

    public SimpleResultSetMetaData(List<Class<?>> types) {
        this.types = types;
    }

    @Override
    public int getColumnCount() throws SQLException {
        return types.size();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        return types.get(column - 1).getName();
    }

    public String getColumnLabel(int column) throws SQLException {
        return Integer.toString(column);
    }

    public int getColumnType(int column) throws SQLException {
        if (types.get(column - 1).isAssignableFrom(Integer.class)) {
            return Types.INTEGER;
        } else {
            return Types.VARCHAR;
        }

    }
}
