package com.intuit.idea.ziplock.core.utils.containers;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * a class to hold all the unimplemented crap
 */
abstract class AbstractResultSet {
    protected static final String NOT_SUPPORTED = "This implementation does not support this method";

    public boolean next() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void close() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean wasNull() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public String getString(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean getBoolean(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public byte getByte(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public short getShort(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public int getInt(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public long getLong(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public float getFloat(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public double getDouble(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public byte[] getBytes(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Date getDate(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Time getTime(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public String getString(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean getBoolean(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public byte getByte(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public short getShort(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public int getInt(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public long getLong(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public float getFloat(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public double getDouble(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public byte[] getBytes(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Date getDate(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Time getTime(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public SQLWarning getWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public void clearWarnings() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public ResultSetMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Object getObject(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Object getObject(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public int findColumn(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean isBeforeFirst() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean isAfterLast() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean isFirst() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean isLast() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public void beforeFirst() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public void afterLast() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean first() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean last() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public int getRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean absolute(int row) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean relative(int rows) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean previous() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public int getFetchDirection() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void setFetchDirection(int direction) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public int getFetchSize() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void setFetchSize(int rows) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public int getType() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public int getConcurrency() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Statement getStatement() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public URL getURL(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public URL getURL(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public int getHoldability() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean isClosed() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Array getArray(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Array getArray(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public InputStream getAsciiStream(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public InputStream getAsciiStream(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public InputStream getBinaryStream(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public InputStream getBinaryStream(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Blob getBlob(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Blob getBlob(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Reader getCharacterStream(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Reader getCharacterStream(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Clob getClob(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Clob getClob(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public String getCursorName() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Reader getNCharacterStream(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Reader getNCharacterStream(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public NClob getNClob(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public NClob getNClob(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public String getNString(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public String getNString(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Object getObject(int arg0, Map<String, Class<?>> arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Object getObject(String arg0, Map<String, Class<?>> arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Ref getRef(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public Ref getRef(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }


    public SQLXML getSQLXML(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public SQLXML getSQLXML(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public InputStream getUnicodeStream(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public InputStream getUnicodeStream(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void refreshRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public boolean rowDeleted() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public boolean rowInserted() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public boolean rowUpdated() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    //
    // all the update methods are unsupported, requires a separate statement in Cassandra
    //

    public void updateArray(int arg0, Array arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateArray(String arg0, Array arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateAsciiStream(int arg0, InputStream arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateAsciiStream(int arg0, InputStream arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateAsciiStream(String arg0, InputStream arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateAsciiStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateAsciiStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBigDecimal(String arg0, BigDecimal arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBinaryStream(int arg0, InputStream arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBinaryStream(int arg0, InputStream arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBinaryStream(String arg0, InputStream arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBinaryStream(String arg0, InputStream arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBinaryStream(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBlob(int arg0, Blob arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBlob(int arg0, InputStream arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBlob(String arg0, Blob arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBlob(String arg0, InputStream arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBlob(String arg0, InputStream arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBoolean(int arg0, boolean arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBoolean(String arg0, boolean arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateByte(int arg0, byte arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateByte(String arg0, byte arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBytes(int arg0, byte[] arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateBytes(String arg0, byte[] arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateCharacterStream(int arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateCharacterStream(int arg0, Reader arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateCharacterStream(String arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateCharacterStream(String arg0, Reader arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateClob(int arg0, Clob arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateClob(int arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateClob(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateClob(String arg0, Clob arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateClob(String arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateClob(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateDate(int arg0, Date arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateDate(String arg0, Date arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateDouble(int arg0, double arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateDouble(String arg0, double arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateFloat(int arg0, float arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateFloat(String arg0, float arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateInt(int arg0, int arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateInt(String arg0, int arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateLong(int arg0, long arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateLong(String arg0, long arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNCharacterStream(int arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNCharacterStream(String arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNCharacterStream(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNClob(int arg0, NClob arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNClob(int arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNClob(int arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNClob(String arg0, NClob arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNClob(String arg0, Reader arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNClob(String arg0, Reader arg1, long arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNString(int arg0, String arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNString(String arg0, String arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNull(int arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateNull(String arg0) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateObject(int arg0, Object arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateObject(int arg0, Object arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateObject(String arg0, Object arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateObject(String arg0, Object arg1, int arg2) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateRef(int arg0, Ref arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateRef(String arg0, Ref arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateRowId(int arg0, RowId arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateRowId(String arg0, RowId arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateShort(int arg0, short arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateShort(String arg0, short arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateString(int arg0, String arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateString(String arg0, String arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateTime(int arg0, Time arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateTime(String arg0, Time arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }

    public void updateTimestamp(String arg0, Timestamp arg1) throws SQLException {
        throw new SQLFeatureNotSupportedException(NOT_SUPPORTED);
    }
}