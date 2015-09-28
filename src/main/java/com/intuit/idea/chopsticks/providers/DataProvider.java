package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.services.ComparisonService;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2015
 *
 * @author albert
 */

public interface DataProvider extends AutoCloseable {

    void openConnections() throws DataProviderException;

    void closeConnections();

    ResultSets getData(ComparisonService cs) throws DataProviderException;

    ResultSets getData(ComparisonService cs, Map<String, List<String>> pksWithHeaders) throws DataProviderException;

    String getQuery(ComparisonService cs);

    ResultSet getMetadata() throws DataProviderException;

    List<String> getPrimaryKeys() throws DataProviderException;

    VendorType getVendorType();

    DataProviderType getDataProviderType();

    String getName();

}
