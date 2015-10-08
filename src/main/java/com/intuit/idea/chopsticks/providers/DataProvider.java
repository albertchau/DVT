package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.services.ComparisonService;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
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

    ResultSet getData(ComparisonService cs) throws DataProviderException;

    ResultSet getData(ComparisonService cs, Map<String, List<String>> pksWithHeaders) throws DataProviderException;

    String getQuery(ComparisonService cs) throws DataProviderException;

    List<Metadata> getMetadata() throws DataProviderException;

    List<String> getPrimaryKeys() throws DataProviderException;

    VendorType getVendorType();

    DataProviderType getDataProviderType();

    String getName();

}
