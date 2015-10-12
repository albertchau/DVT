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
//import static com.intuit.idea.chopsticks.services.ComparisonServices.COUNT;
//
///**
// * Copyright 2015
// *
// * @author albert
// */
//public class CountComparisonService extends ComparisonServiceBase {
//    private static Logger logger = LoggerFactory.getLogger(CountComparisonService.class);
//
//    public CountComparisonService(Set<ResultStore> resultStores, Loader loader, Extracter extracter, Comparer comparer) {
//        super(resultStores, loader, extracter, comparer);
//    }
//
//    @Override
//    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
//        initializeConnections(source, target);
//        Tuple4<ResultSet, List<Metadata>, ResultSet, List<Metadata>> loaded = loader.load(source, target, COUNT);
//        startComparison(loaded);
//        closeConnections(source, target);
//    }
//
//    /**
//     * Exposed for testing purposes mainly
//     *
//     * @param loaded Tuple that contains the data. source result set, the metadata describing that result set. target result set, the metadata describing that result set.
//     * @throws ComparisonException if something were to go wrong in setup or execution phase
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
