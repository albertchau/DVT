package com.intuit.idea.chopsticks.providers;

import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.services.ComparisonService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface DataProvider {

    void openConnections() throws SQLException;

    void closeConnections() throws SQLException;

    ResultSets getData(ComparisonService cs) throws SQLException;

    ResultSets getData(ComparisonService cs, List<List<String>> sampledPrimaryKeys, List<String> pkColumns) throws SQLException;

    String getQuery(ComparisonService cs);

    ResultSet getMetadata() throws SQLException;

    List<String> getPrimaryKeys() throws SQLException;

    VendorType getVendorType();

    DataProviderType getDataProviderType();

    String getName();

}
