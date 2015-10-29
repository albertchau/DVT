package com.intuit.idea.ziplock.core.services;

import com.intuit.idea.ziplock.core.providers.DataProvider;
import com.intuit.idea.ziplock.core.results.ResultStore;
import com.intuit.idea.ziplock.core.services.strategies.comparers.Comparer;
import com.intuit.idea.ziplock.core.services.strategies.comparers.DefaultComparer;
import com.intuit.idea.ziplock.core.services.strategies.extractors.DefaultExtractor;
import com.intuit.idea.ziplock.core.services.strategies.extractors.Extractor;
import com.intuit.idea.ziplock.core.services.strategies.loaders.DefaultLoader;
import com.intuit.idea.ziplock.core.services.strategies.loaders.Loader;
import com.intuit.idea.ziplock.core.utils.Commons;
import com.intuit.idea.ziplock.core.utils.containers.Extracted;
import com.intuit.idea.ziplock.core.utils.containers.Loaded;
import com.intuit.idea.ziplock.core.utils.exceptions.ComparisonException;
import com.intuit.idea.ziplock.core.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Pre-steps:
 * 1) Initialize connections
 * <p>
 * Three Steps...
 * 1) Load: (DataProvider, DataProvider) -> Loaded
 * 2) Extract: Loaded -> Extracted
 * 3) Compare: Extracted -> void
 * <p>
 * Post-steps:
 * 1) Close connections
 *
 * @author albert
 */
public final class ComparisonServiceImpl implements ComparisonService {
    private static final Logger logger = LoggerFactory.getLogger(ComparisonServiceImpl.class);
    private final Set<ResultStore> resultStores;
    private final Loader loader;
    private final Extractor extractor;
    private final Comparer comparer;
    private final ComparisonType comparisonType;
    private Long startTotal;

    /**
     * Constructor with injected Result Stores
     *
     * @param resultStores   to store the results
     * @param comparisonType the type of comparison we are doing
     */
    public ComparisonServiceImpl(Set<ResultStore> resultStores, Loader loader, Extractor extractor, Comparer comparer, ComparisonType comparisonType) {
        this.comparisonType = comparisonType;
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
        this.loader = loader == null ? new DefaultLoader() : loader;
        this.extractor = extractor == null ? new DefaultExtractor() : extractor;
        this.comparer = comparer == null ? new DefaultComparer() : comparer;
    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        startTotal = System.nanoTime();
        logger.info("Starting " + comparisonType.stringify() + " for " + Commons.identifierName(source, target) + ".");
        initializeConnections(source, target);
        Loaded loaded = loader.load(source, target, comparisonType); //step one
        logger.debug("Loading Time = " + ((System.nanoTime() - startTotal) / 1000000) + " ms.");
        startComparison(loaded);
        closeConnections(source, target);
        logger.info((comparer.getResult() ? "[PASS] " : "[FAIL] ") +
                comparisonType.stringify() +
                " for " +
                Commons.identifierName(source, target) +
                ". Total Elapsed = " +
                ((System.nanoTime() - startTotal) / 1000000) +
                " ms.");
    }

    @Override
    public void startComparison(Loaded loaded) throws ComparisonException {
        long startLeg = System.nanoTime();
        Extracted extracted = extractor.extract(loaded); //step two
        logger.debug("Extracting Time = " + ((System.nanoTime() - startLeg) / 1000000) + " ms.");
        startLeg = System.nanoTime();
        comparer.compare(extracted, resultStores); // step three
        logger.debug("Comparing Time = " + ((System.nanoTime() - startLeg) / 1000000) + " ms.");
    }

    @Override
    public ComparisonType getType() {
        return comparisonType;
    }

    private void closeConnections(DataProvider source, DataProvider target) {
        source.closeConnections();
        target.closeConnections();
    }

    private void initializeConnections(DataProvider source, DataProvider target) throws ComparisonException {
        try {
            source.openConnections();
        } catch (DataProviderException e) {
            throw new ComparisonException("Could not establish connections to source: " + e.getMessage(), e);
        }
        try {
            target.openConnections();
        } catch (DataProviderException e) {
            throw new ComparisonException("Could not establish connections to target: " + e.getMessage(), e);
        }
    }

}
