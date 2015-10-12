package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.services.strategies.*;
import com.intuit.idea.chopsticks.services.transforms.Extracted;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Pre-steps:
 * 1) Initialize connections
 *
 * Three Steps...
 * 1) Load: (DataProvider, DataProvider) -> Loaded
 * 2) Extract: Loaded -> Extracted
 * 3) Compare: Extracted -> void
 *
 * Post-steps:
 * 1) Close connections
 *
 * @author albert
 */
public final class ComparisonServiceImpl implements ComparisonService {
    private static final Logger logger = LoggerFactory.getLogger(ComparisonServiceImpl.class);
    private final Set<ResultStore> resultStores;
    private final Loader loader;
    private final Extracter extracter;
    private final Comparer comparer;
    private final ComparisonServices typeOfComparison;

    /**
     * Constructor with injected Result Stores
     * @param resultStores to store the results
     * @param typeOfComparison the type of comparison we are doing
     */
    public ComparisonServiceImpl(Set<ResultStore> resultStores, Loader loader, Extracter extracter, Comparer comparer, ComparisonServices typeOfComparison) {
        this.typeOfComparison = typeOfComparison;
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
        this.loader = loader == null ? new DefaultLoader(): loader;
        this.extracter = extracter == null ? new DefaultExtracter(): extracter;
        this.comparer = comparer == null ? new DefaultComparer(): comparer;
    }
    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        initializeConnections(source, target);
        Loaded loaded = loader.load(source, target, typeOfComparison); //step one
        startComparison(loaded);
        closeConnections(source, target);
    }

    @Override
    public void startComparison(Loaded loaded) throws ComparisonException {
        long start = System.nanoTime();
        Extracted extracted = extracter.extract(loaded); //step two
        comparer.compare(extracted, resultStores); // step three
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    private void closeConnections(DataProvider source, DataProvider target) {
        source.closeConnections();
        target.closeConnections();
    }

    private void initializeConnections(DataProvider source, DataProvider target) throws ComparisonException {
        try {
            source.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not establish connections to source.");
        }
        try {
            target.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not establish connections to target.");
        }
    }

}
