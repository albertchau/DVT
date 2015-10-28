package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.services.ComparisonType;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.containers.ResultSets;
import com.intuit.idea.chopsticks.utils.containers.SimpleResultSet;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.intuit.idea.chopsticks.utils.ComparisonUtils.extractSpecifiedMetadata;
import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/12/15
 * ************************************
 */
public class AdhocJdbcDataProvider extends JdbcDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(AdhocJdbcDataProvider.class);
    private final String query;
    private ResultSet data = null;
    private List<String> primaryKeys;

    public AdhocJdbcDataProvider(VendorType vendor, String host, String port, String url, String user, String password,
                                 String database, String hivePrincipal, String name,
                                 List<? extends JdbcDataProvider> shards, String query) {
        super(vendor, host, port, url, user, password, database, hivePrincipal, name, shards);
        this.query = query;
    }

    @Override
    public ResultSet getData(ComparisonType cs) throws DataProviderException {
        if (isNull(data)) {
            data = getData(query);
        }
        if (cs == ComparisonType.COUNT) {
            try {
                List<List<Object>> rows = new ArrayList<>();
                while (data.next()) {
                    int size = 0;
                    data.last();
                    size += data.getRow();
                    List<Object> columns = singletonList(size);
                    rows.add(columns);
                }
                return new SimpleResultSet(rows);
            } catch (SQLException e) {
                logger.error("Could not mock up count from adhoc query: " + e.getMessage());
                throw new DataProviderException("Could not mock up count from adhoc query.", e);
            }
        }
        return data;
    }

    @Override
    public ResultSets getData(ComparisonType cs, Map<String, List<String>> pksWithHeaders) {
        logger.error("Adhoc query does not support systematic sampling by specifying primary keys.");
        throw new UnsupportedOperationException("Adhoc query does not support systematic sampling by specifying primary keys.");
    }

    @Override
    public String getQuery(ComparisonType cs) {
        return query;
    }

    @Override
    public List<Metadata> getMetadata() throws DataProviderException {
        if (data == null) {
            logger.warn("Cannot get metadata because data query hasn't run yet. Running now...");
            data = getData(query);
        }
        try {
            return extractSpecifiedMetadata(data, null, primaryKeys);
        } catch (SQLException e) {
            throw new DataProviderException("Could not extract metadata from adhoc query.", e);
        }
    }

    @Override
    public List<String> getPrimaryKeys() {
        return primaryKeys == null ? EMPTY_LIST : primaryKeys;
    }

    @Override
    public DataProviderType getDataProviderType() {
        return DataProviderType.QUERY;
    }
}
