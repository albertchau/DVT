package com.intuit.idea.chopsticks.utils;

import com.intuit.idea.chopsticks.utils.exceptions.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.intuit.idea.chopsticks.utils.Pair.cons;

/**
 * An implementation of an iterator which wraps a row in a result set as a Tuple
 */
public class ResultSetIterator implements Iterator<Tuple> {

    private final Connection connection;
    private final String sql;
    private Logger logger = LoggerFactory.getLogger(ResultSetIterator.class);
    private ResultSet rs;
    private PreparedStatement ps;
    // todo Integer might be able to be turned into Class<?> using resultsetmetadata's getColumnClassName
    private Pair<String, Integer>[] metadata;

    public ResultSetIterator(Connection connection, String sql) {
        if (connection == null || sql == null) {
            throw new AssertionError();
        }
        this.connection = connection;
        this.sql = sql;
    }

    public ResultSetIterator(ResultSet rs) {
        this.rs = rs;
        connection = null;
        sql = null;
        ps = null;
    }

    public void init() {
        if (connection == null) {
            logger.error("Cannot init result set iterator because you passed in result set and not connection.");
            throw new DataAccessException("Cannot init result set iterator because you passed in result set and not connection.");
        }
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            close();
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (ps == null && connection != null) {
            init();
        }
        try {
            boolean hasMore = rs.next();
            if (!hasMore) {
                close();
            }
            return hasMore;
        } catch (SQLException e) {
            close();
            throw new DataAccessException(e);
        }

    }

    void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            //nothing we can do here
            e.printStackTrace();
        }
    }

    @Override
    public Tuple next() {
        try {
            return nextRowAsTuple();
        } catch (DataAccessException e) {
            close();
            throw e;
        }
    }

    private Tuple nextRowAsTuple() {
        try {
            if (metadata == null) {
                metadata = getMetadata();
            }

            Stream<Pair<String, Integer>> stream = StreamSupport.stream(Spliterators.spliterator(metadata, 0), false);
            Collection<Pair<String, ?>> result = stream
                    .map(o -> {
                        try {
                            String field = o.getCar();
                            Object value = rs.getObject(field);
                            return cons(field, value);
                        } catch (SQLException e) {
                            throw new DataAccessException(e);
                        }
                    }).collect(Collectors.toList());
            return new ResultsTuple(result);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Pair<String, Integer>[] getMetadata() throws SQLException {
        int columnCount;
        ResultSetMetaData rsmd = rs.getMetaData();
        try {
            columnCount = rsmd.getColumnCount();
            Pair<String, Integer>[] metadata = new Pair[columnCount];
            for (int i = 0; i < columnCount; i++) {
                Integer type = rsmd.getColumnType(i + 1);
                String name = rsmd.getColumnName(i + 1);
                metadata[i] = cons(name, type);
            }
            return metadata;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}