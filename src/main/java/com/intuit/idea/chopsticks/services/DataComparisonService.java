package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DataComparisonService extends ComparisonServiceBase {
    private static Logger logger = LoggerFactory.getLogger(DataComparisonService.class);

    public DataComparisonService(Set<ResultStore> resultStores) {
        super(resultStores);
    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        initializeConnections(source, target);
        try (ResultSet srcData = source.getData(this);
             ResultSet tarData = target.getData(this)) {
            List<Metadata> srcMetadata = source.getMetadata();
            List<Metadata> tarMetadata = target.getMetadata();
            startComparison(srcData, srcMetadata,
                    tarData, tarMetadata);
        } catch (DataProviderException | SQLException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
        closeConnections(source, target);
    }

    /**
     * Exposing this method so that we can do testing on it. Ultimately uses merge sort/compare to do comparison. Steps:
     * 1) Compares source/target metadata and
     * and creates an ordered master list of type {@link Metadata Metadata} that will determine which columns from the source and target result set to query
     * 2) Creates a List of Object[] from the srcData using the metadata from step 1. This represents rows and its column contents.
     * 3) Creates a List of Object[] from the tarData using the metadata from step 1. This represents rows and its column contents.
     * 4) Starts Merge Sort/Compare by sorting both lists based on primary keys.
     * 5) Iterate through sourceList and targetList (step 2 and 3)
     * 6) Compare at each index only iterating one of the iterators if it cannot be paired and is less than its counterpart. Iterate both if can be compared.
     * 7) Report out results to {@link ResultStore resultStore}
     *
     * @param srcData     ResultSet of data to be compared as source against target
     * @param tarData     ResultSet of data to be compared as target against source
     */
    public void startComparison(ResultSet srcData, List<Metadata> srcMetadata,
                                ResultSet tarData, List<Metadata> tarMetadata) throws ComparisonException {
        long start = System.nanoTime();
        Metadata[] sMetadataArr = srcMetadata.toArray(new Metadata[srcMetadata.size()]);
        Metadata[] tMetadataArr = tarMetadata.toArray(new Metadata[tarMetadata.size()]);
        comparisonStrategy(srcData, tarData, sMetadataArr, tMetadataArr);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

}