package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.services.strategies.*;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;

import java.util.Set;

import static com.intuit.idea.chopsticks.services.ComparisonServices.*;

/**
 * BiConsumer-esque
 * interface for what the comparison service does.
 *
 * @author albert
 */
public interface ComparisonService {

    void compare(DataProvider source, DataProvider target) throws ComparisonException;

    void startComparison(Loaded loaded) throws ComparisonException;
    
    static ComparisonService createForMetadata(Set<ResultStore> resultStores) {
        return new ComparisonServiceImpl(resultStores, new MetadataLoader(), new MetadataExtracter(), new MetadataComparer(), null);
    }

    static ComparisonService createForCount(Set<ResultStore> resultStores) throws ComparisonException {
        return new ComparisonServiceImpl(resultStores, new DefaultLoader(), new CountExtracter(), new CountComparer(0.0), COUNT);
    }

    static ComparisonService createForExistence(Set<ResultStore> resultStores) {
        return new ComparisonServiceImpl(resultStores, new DefaultLoader(), new ExistenceExtracter(), new DefaultComparer(), EXISTENCE);
    }
    
    static ComparisonService createForData(Set<ResultStore> resultStores) {
        return new ComparisonServiceImpl(resultStores, new DefaultLoader(), new DefaultExtracter(), new DefaultComparer(), DATA);
    }

}
