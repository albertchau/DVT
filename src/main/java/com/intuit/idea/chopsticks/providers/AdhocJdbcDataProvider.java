package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.services.ComparisonService;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/12/15
 * ************************************
 */
public class AdhocJdbcDataProvider extends JdbcDataProvider {
    public AdhocJdbcDataProvider(VendorType vendor, String host, String port, String url, String user, String password, String database, String hivePrincipal, String name, List<? extends JdbcDataProvider> shards) {
        super(vendor, host, port, url, user, password, database, hivePrincipal, name, shards);
    }

    @Override
    public ResultSets getData(ComparisonService cs) {
        return null;
    }

    @Override
    public ResultSets getData(ComparisonService cs, Map<String, List<String>> pksWithHeaders) {
        return null;
    }

    @Override
    public String getQuery(ComparisonService cs) {
        return null;
    }

    @Override
    public ResultSet getMetadata() {
        return null;
    }

    @Override
    public List<String> getPrimaryKeys() {
        return null;
    }

    @Override
    public DataProviderType getDataProviderType() {
        return null;
    }

    @Override
    public void close() {

    }
}
