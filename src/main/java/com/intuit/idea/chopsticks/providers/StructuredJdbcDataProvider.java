package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.query.QueryService;
import com.intuit.idea.chopsticks.services.ComparisonType;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

import static com.intuit.idea.chopsticks.utils.SQLTypeMap.toClass;
import static java.util.stream.Collectors.toList;

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
    public ResultSet getData(ComparisonType cs) throws DataProviderException {
        String query = getQuery(cs);
        return getData(query);
    }

    @Override
    public ResultSet getData(ComparisonType cs, Map<String, List<String>> pksWithHeaders) throws DataProviderException {
        return getData(getQuery(cs, pksWithHeaders));
    }

    @Override
    public String getQuery(ComparisonType cs) throws DataProviderException {
        switch (cs) {
            case DATA:
                return queryService.createDataQuery(getMetadata());
            case EXISTENCE:
                return queryService.createExistenceQuery(getMetadata());
            case COUNT:
                return queryService.createCountQuery();
            case METADATA:
                logger.error("Cannot create query for metadata comparison type.");
                throw new UnsupportedOperationException("Cannot create query for metadata comparison type.");
            default:
                logger.error("Unknown Comparison Service");
                throw new UnsupportedOperationException("Unknown Comparison Service");
        }
    }

    @Override
    public List<Metadata> getMetadata() throws DataProviderException {
        //todo check for specified
        try {
            List<Metadata> metadatas = getFromDatabaseUsing(this::metadataFromDatabase);
            Collections.sort(metadatas);
            return metadatas;
        } catch (DataProviderException e) {
            throw new DataProviderException(e.getMessage());
        }
    }

    @Override
    public List<String> getPrimaryKeys() throws DataProviderException {
        //todo check for specified
        try {
            List<String> primaryKeys = getFromDatabaseUsing(this::primaryKeysFromDatabase);
            Collections.sort(primaryKeys);
            return primaryKeys;
        } catch (DataProviderException e) {
            throw new DataProviderException(e.getMessage());
        }
    }

    @Override
    public DataProviderType getDataProviderType() {
        return DataProviderType.JDBC;
    }

    private String getQuery(ComparisonType cs, Map<String, List<String>> pksWithHeaders) throws DataProviderException {
        switch (cs) {
            case DATA:
                return queryService.createDataQueryWithInputSamples(getMetadata(), pksWithHeaders);
            case EXISTENCE:
                return queryService.createExistenceQueryWithInputSamples(getMetadata(), pksWithHeaders);
            case COUNT:
                logger.info("Cannot use sampling for Count Comparison Service.");
                throw new UnsupportedOperationException("Cannot use sampling for Count Comparison Service.");
            case METADATA:
                logger.error("Cannot use sampling for Metadata Comparison Service.");
                throw new UnsupportedOperationException("Cannot use sampling for Metadata Comparison Service.");
            default:
                logger.info("Unknown/Unsupported Comparison Service for sampling");
                throw new UnsupportedOperationException("Unknown/Unsupported Comparison Service for sampling");
        }
    }

    private List<Metadata> metadataFromDatabase(Connection c) throws DataProviderException {
        try {
            ResultSet rs = c.getMetaData().getColumns(null, "%", getName(), "%");
            List<Metadata> metadatas = new ArrayList<>();
            List<String> primaryKeys = getPrimaryKeys();
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                Integer sqlTypeInt = rs.getInt("DATA_TYPE");
                Class<? extends Comparable> javaType = toClass(sqlTypeInt);
                Boolean isPk = primaryKeys.stream()
                        .anyMatch(pk -> pk.equalsIgnoreCase(columnName));
                metadatas.add(Metadata.createWithNoAliasing(columnName, isPk, javaType));
            }
            return metadatas;
        } catch (DataProviderException | SQLException e) {
            throw new DataProviderException(e.getMessage());
        }
    }

    private <T> List<T> getFromDatabaseUsing(Function<Connection, List<T>> databaseConsumer) throws DataProviderException {
        if (vendor.equals(VendorType.HIVE_1)) {
            throw new NotImplementedException();
        } else {
            List<List<T>> listsOfLists = getConnections().stream()
                    .map(databaseConsumer)
                    .filter(Objects::nonNull)
                    .collect(toList());
            if (listsOfLists.isEmpty()) {
                throw new DataProviderException("Could not get data from database. All connections returned null.");
            }
            if (listsOfLists.size() < 1) {
                throw new DataProviderException("Could not get any data for this data provider for all result sets.");
            } else if (listsOfLists.size() > 1) {
                List<T> masterCopy = listsOfLists.get(0);
                if (!listsOfLists.stream().allMatch(masterCopy::containsAll)) {
                    throw new DataProviderException("The data across the shards/connections differ.");
                }
            }
            return listsOfLists.get(0);
        }
    }

    private List<String> primaryKeysFromDatabase(Connection c) throws DataProviderException {
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
            throw new DataProviderException(e.getMessage());
        }
    }

}
