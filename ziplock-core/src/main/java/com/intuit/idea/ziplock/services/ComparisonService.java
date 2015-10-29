package com.intuit.idea.ziplock.services;

import com.intuit.idea.ziplock.providers.DataProvider;
import com.intuit.idea.ziplock.results.ResultStore;
import com.intuit.idea.ziplock.services.strategies.comparers.CountComparer;
import com.intuit.idea.ziplock.services.strategies.comparers.DefaultComparer;
import com.intuit.idea.ziplock.services.strategies.comparers.MetadataComparer;
import com.intuit.idea.ziplock.services.strategies.extractors.CountExtractor;
import com.intuit.idea.ziplock.services.strategies.extractors.DefaultExtractor;
import com.intuit.idea.ziplock.services.strategies.extractors.ExistenceExtractor;
import com.intuit.idea.ziplock.services.strategies.extractors.MetadataExtractor;
import com.intuit.idea.ziplock.services.strategies.loaders.DefaultLoader;
import com.intuit.idea.ziplock.services.strategies.loaders.MetadataLoader;
import com.intuit.idea.ziplock.utils.containers.Loaded;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.intuit.idea.ziplock.services.ComparisonType.*;

/**
 * BiConsumer-esque
 * interface for what the comparison service does.
 *
 * @author albert
 */
public interface ComparisonService {

    static ComparisonService createForMetadata(Set<ResultStore> resultStores) {
        return new ComparisonServiceImpl(resultStores, new MetadataLoader(), new MetadataExtractor(), new MetadataComparer(), METADATA);
    }

    static ComparisonService createForCount(Set<ResultStore> resultStores) throws ComparisonException {
        return new ComparisonServiceImpl(resultStores, new DefaultLoader(), new CountExtractor(), new CountComparer(0.0), COUNT);
    }

    static ComparisonService createForExistence(Set<ResultStore> resultStores) {
        return new ComparisonServiceImpl(resultStores, new DefaultLoader(), new ExistenceExtractor(), new DefaultComparer(), EXISTENCE);
    }

    static ComparisonService createForData(Set<ResultStore> resultStores) {
        return new ComparisonServiceImpl(resultStores, new DefaultLoader(), new DefaultExtractor(), new DefaultComparer(), DATA);
    }

    static List<ComparisonService> createAllComparisons(Set<ResultStore> resultStores) throws ComparisonException {
        List<ComparisonService> comparisonServiceList = new ArrayList<>();
        comparisonServiceList.add(createForMetadata(resultStores));
        comparisonServiceList.add(createForCount(resultStores));
        comparisonServiceList.add(createForExistence(resultStores));
        comparisonServiceList.add(createForData(resultStores));
        return comparisonServiceList;
    }

    void compare(DataProvider source, DataProvider target) throws ComparisonException;

    void startComparison(Loaded loaded) throws ComparisonException;

    ComparisonType getType();

}
