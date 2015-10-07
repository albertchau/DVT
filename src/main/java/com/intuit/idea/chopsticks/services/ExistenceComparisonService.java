package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import static com.intuit.idea.chopsticks.services.ComparisonUtils.extractSpecifiedMetadata;

/**
 * Copyright 2015
 * 1) get metadata map to comparable Java
 * 2) get data
 * 3) use metadata to cast each data column when converting to int
 * 4) find colcompareto
 * 5) create combined metadata from both metadatas and colcompareto
 *
 * @author albert
 */
public class ExistenceComparisonService extends ComparisonServiceBase {
    private static final Logger logger = LoggerFactory.getLogger(ExistenceComparisonService.class);

    /**
     * Constructor with injected Result Stores
     *
     * @param resultStores to store the results
     */
    public ExistenceComparisonService(Set<ResultStore> resultStores) {
        super(resultStores);
    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        initializeConnections(source, target);
        try (ResultSet srcRowSet = source.getData(this);
             ResultSet tarRowSet = target.getData(this)) {
            List<String> srcPrimaryKeys = source.getPrimaryKeys();
            List<String> tarPrimaryKeys = target.getPrimaryKeys();
            startComparison(srcRowSet, tarRowSet, srcPrimaryKeys, tarPrimaryKeys);
        } catch (DataProviderException | SQLException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
        closeConnections(source, target);
    }

    /**
     * Exposed for testing purposes. Used for when the result sets are not column ordered
     * and need some work done using defined metadata before comparing.
     * Will do a sort on significance using the primary key's lexigraphical ordering.
     *
     * @param srcRowSet Source result set that is to be compared with target.
     * @param tarRowSet Target result set that is to be compared with source.
     * @param sPks      Source primary key columns. Must exist in source result set columns. Order nor case matter.
     * @param tPks      Target primary key columns. Must exist in target result set columns. Order nor case matter.
     * @throws ComparisonException sdf
     */
    public void startComparison(ResultSet srcRowSet, ResultSet tarRowSet, List<String> sPks, List<String> tPks) throws ComparisonException {
        long start = System.nanoTime();
        Metadata[] sMetadata;
        Metadata[] tMetadata;
        try {
            sMetadata = extractSpecifiedMetadata(srcRowSet, sPks, sPks);
            tMetadata = extractSpecifiedMetadata(tarRowSet, tPks, tPks);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not connect to or get metadata from source or target databases.");
        }
        comparisonStrategy(srcRowSet, tarRowSet, sMetadata, tMetadata);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

}
