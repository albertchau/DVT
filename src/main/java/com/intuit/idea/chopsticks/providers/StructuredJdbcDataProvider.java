package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.query.QueryService;
import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.services.ComparisonService;
import com.intuit.idea.chopsticks.services.CountComparisonService;
import com.intuit.idea.chopsticks.services.DataComparisonService;
import com.intuit.idea.chopsticks.services.ExistenceComparisonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/12/15
 * ************************************
 */
public final class StructuredJdbcDataProvider extends JdbcDataProvider {
    private Logger logger = LoggerFactory.getLogger(StructuredJdbcDataProvider.class);

    private QueryService queryService;

    public StructuredJdbcDataProvider(VendorType vendor, String host, String port, String url, String user, String password,
                                      String database, String hivePrincipal, String name,
                                      List<StructuredJdbcDataProvider> shards, QueryService queryService) {
        super(vendor, host, port, url, user, password, database, hivePrincipal, name, shards);
        this.queryService = queryService;
    }

    @Override
    public ResultSets getData(ComparisonService cs) throws SQLException {
        return getData(getQuery(cs));
    }

    private ResultSets getData(String query) throws SQLException {
        if (connections == null) {
            logger.error("You need to openConnections() before calling this getData() method.");
            throw new SQLException("You need to openConnections() before calling this getData() method.");
        }
        if (connections.size() < 1) {
            logger.error("There are no active connections open");
            throw new SQLException("There are no active connections open");
        }

        List<ResultSet> resultSetStream = connections.stream()
                .map(c -> {
                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = c.createStatement();
                        rs = stmt.executeQuery(query);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        logger.error("SQLException: " + ex.getMessage());
                    }
                    return rs;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (resultSetStream.size() < 1) {
            logger.error("Could not get any resultSets. they were all null");
            throw new SQLException("Could not get any resultSets. they were all null");
        }

        return new ResultSets(resultSetStream);
    }

    @Override
    public ResultSets getData(ComparisonService cs, List<List<String>> sampledPrimaryKeys, List<String> pkColumns) throws SQLException {
        return getData(getQuery(cs, sampledPrimaryKeys, pkColumns));
    }

    @Override
    public String getQuery(ComparisonService cs) {
        if (cs instanceof CountComparisonService) {
            return queryService.createCountQuery();
        } else if (cs instanceof DataComparisonService) {
            return queryService.createDataQuery();
        } else if (cs instanceof ExistenceComparisonService) {
            return queryService.createExistenceQuery();
        } else {
            logger.info("Unknown Comparison Service");
            throw new UnsupportedOperationException("Unknown Comparison Service");
        }
    }

    private String getQuery(ComparisonService cs, List<List<String>> sampledPrimaryKeys, List<String> pkColumns) {
        if (cs instanceof CountComparisonService) {
            logger.info("Cannot use sampling for Count Comparison Service.");
            throw new UnsupportedOperationException("Cannot use sampling for Count Comparison Service.");
        } else if (cs instanceof DataComparisonService) {
            return queryService.createDataQuery(sampledPrimaryKeys, pkColumns);
        } else if (cs instanceof ExistenceComparisonService) {
            return queryService.createExistenceQuery(sampledPrimaryKeys, pkColumns);
        } else {
            logger.info("Unknown/Unsupported Comparison Service for sampling");
            throw new UnsupportedOperationException("Unknown/Unsupported Comparison Service for sampling");
        }
    }

    @Override
    public ResultSet getMetadata() throws SQLException {
        if (vendor.equals(VendorType.HIVE_1)) {
            //todo: hive_1 bs... like describe table and such
            throw new NotImplementedException();
        } else {
            List<ResultSet> tableMetadataStream = getConnections().stream()
                    .map(c -> {
                        try {
                            return c.getMetaData().getColumns(null, "%", getName(), "%");
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (tableMetadataStream.isEmpty()) {
                logger.error("Could not get metadata. All connections returned null");
                throw new SQLException("Could not get metadata. All connections returned null");
            } else {
                //todo: maybe make sure they are all the same if it is sharded
                return tableMetadataStream.get(0);
            }
        }
    }

    @Override
    public List<String> getPrimaryKeys() throws SQLException {
        if (vendor.equals(VendorType.HIVE_1)) {
            //todo: hive_1 bs... like describe table and such
            throw new NotImplementedException();
        } else {
            List<List<String>> primaryKeys = getConnections().stream()
                    .map(c -> {
                        try {
                            String columnNameColumn = "COLUMN_NAME";
                            ResultSet columns = c.getMetaData().getPrimaryKeys(null, "%", getName());
                            List<String> colNames = new ArrayList<>();
                            while (columns.next()) {
                                String colName = columns.getString(columnNameColumn).trim();
                                colNames.add(colName);

                            }
                            return colNames;
                        } catch (SQLException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (primaryKeys.isEmpty()) {
                logger.error("Could not get primary keys for metadata. All connections returned null");
                throw new SQLException("Could not get primary keys for metadata. All connections returned null");
            } else {
                //todo: maybe make sure they are all the same if it is sharded
                return primaryKeys.get(0);
            }
        }
    }

    @Override
    public DataProviderType getDataProviderType() {
        return DataProviderType.JDBC;
    }
}
