package com.intuit.idea.ziplock.utils.containers;

import java.sql.SQLException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public abstract class AbstractResultSetMetaData {
    public int getColumnCount() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isAutoIncrement(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCaseSensitive(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isSearchable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCurrency(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int isNullable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isSigned(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getColumnDisplaySize(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnLabel(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getSchemaName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getPrecision(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getScale(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getTableName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getCatalogName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getColumnType(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnTypeName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isWritable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isDefinitelyWritable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnClassName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }
}