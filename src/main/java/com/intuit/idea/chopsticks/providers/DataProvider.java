package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.services.ComparisonService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface DataProvider {

    void openConnections() throws SQLException;

    void closeConnections() throws SQLException;

    ResultSets getData(ComparisonService cs) throws SQLException;

    //todo make them pairs.. to ensure that they are same lengths -- map
    ResultSets getData(ComparisonService cs, Map<String, List<String>> pksWithHeaders) throws SQLException;

    String getQuery(ComparisonService cs);

    ResultSet getMetadata() throws SQLException;

    List<String> getPrimaryKeys() throws SQLException;

    VendorType getVendorType();

    DataProviderType getDataProviderType();

    String getName();

}
