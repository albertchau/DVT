package com.intuit.idea.ziplock.providers;

import com.intuit.idea.ziplock.services.ComparisonType;
import com.intuit.idea.ziplock.utils.containers.Metadata;
import com.intuit.idea.ziplock.utils.exceptions.DataProviderException;

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

    ResultSet getData(ComparisonType cs) throws DataProviderException;

    ResultSet getData(ComparisonType cs, Map<String, List<String>> pksWithHeaders) throws DataProviderException;

    String getQuery(ComparisonType cs) throws DataProviderException;

    List<Metadata> getMetadata() throws DataProviderException;

    List<String> getPrimaryKeys() throws DataProviderException;

    VendorType getVendorType();

    DataProviderType getDataProviderType();

    String getTableName();

}
