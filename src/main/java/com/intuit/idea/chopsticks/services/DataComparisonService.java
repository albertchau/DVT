//package com.intuit.idea.chopsticks.services;
//
//import com.intuit.idea.chopsticks.providers.DataProvider;
//import com.intuit.idea.chopsticks.results.ResultStore;
//import com.intuit.idea.chopsticks.services.strategy.Comparer;
//import com.intuit.idea.chopsticks.services.strategy.Extracter;
//import com.intuit.idea.chopsticks.services.strategy.Loader;
//import com.intuit.idea.chopsticks.services.transforms.Loaded;
//import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;
//import com.intuit.idea.chopsticks.utils.containers.Metadata;
//import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
//import org.jooq.lambda.tuple.Tuple3;
//import org.jooq.lambda.tuple.Tuple4;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.ResultSet;
//import java.util.List;
//import java.util.Set;
//
//import static com.intuit.idea.chopsticks.services.ComparisonServices.DATA;
//
///**
// * Copyright 2015
// *
// * @author albert
// */
//public class DataComparisonService extends ComparisonServiceBase {
//    private static Logger logger = LoggerFactory.getLogger(DataComparisonService.class);
//
//    public DataComparisonService(Set<ResultStore> resultStores, Loader loader, Extracter extracter, Comparer comparer) {
//        super(resultStores, loader, extracter, comparer);
//    }
//
//    @Override
//    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
//        initializeConnections(source, target);
//        Tuple4<ResultSet, List<Metadata>, ResultSet, List<Metadata>> loaded = loader.load(source, target, DATA);
//        startComparison(loaded);
//        closeConnections(source, target);
//    }
//
//    /**
//     * Exposing this method so that we can do testing on it. Ultimately uses merge sort/compare to do comparison. Steps:
//     * 1) Compares source/target metadata and
//     * and creates an ordered master list of type {@link Metadata Metadata} that will determine which columns from the source and target result set to query
//     * 2) Creates a List of Object[] from the srcData using the metadata from step 1. This represents rows and its column contents.
//     * 3) Creates a List of Object[] from the tarData using the metadata from step 1. This represents rows and its column contents.
//     * 4) Starts Merge Sort/Compare by sorting both lists based on primary keys.
//     * 5) Iterate through sourceList and targetList (step 2 and 3)
//     * 6) Compare at each index only iterating one of the iterators if it cannot be paired and is less than its counterpart. Iterate both if can be compared.
//     * 7) Report out results to {@link ResultStore resultStore}
//     *
//     * @param loaded Tuple that contains the data. source result set, the metadata describing that result set. target result set, the metadata describing that result set.
//     * @throws ComparisonException
//     */
//    public void startComparison(Loaded loaded) throws ComparisonException {
//        long start = System.nanoTime();
//        Tuple3<List<Comparable[]>, List<Comparable[]>, CombinedMetadata[]> extracted = extracter.extract(loaded);
//        comparer.compare(extracted, resultStores);
//        long end = System.nanoTime();
//        logger.info("start - end /100000 = " + ((end - start) / 1000000));
//    }
//
//}