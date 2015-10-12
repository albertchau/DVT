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
//import static com.intuit.idea.chopsticks.services.ComparisonServices.EXISTENCE;
//
///**
// * Copyright 2015
// * 1) get metadata map to comparable Java
// * 2) get data
// * 3) use metadata to cast each data column when converting to int
// * 4) find colcompareto
// * 5) create combined metadata from both metadatas and colcompareto
// *
// * @author albert
// */
//public class ExistenceComparisonService extends ComparisonServiceBase {
//    private static final Logger logger = LoggerFactory.getLogger(ExistenceComparisonService.class);
//
//    public ExistenceComparisonService(Set<ResultStore> resultStores, Loader loader, Extracter extracter, Comparer comparer) {
//        super(resultStores, loader, extracter, comparer);
//    }
//
//    @Override
//    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
//        initializeConnections(source, target);
//        Tuple4<ResultSet, List<Metadata>, ResultSet, List<Metadata>> loaded = loader.load(source, target, EXISTENCE);
//        startComparison(loaded);
//        closeConnections(source, target);
//    }
//
//    /**
//     * Exposed for testing purposes. Used for when the result sets are not column ordered
//     * and need some work done using defined metadata before comparing.
//     * Will do a sort on significance using the primary key's lexigraphical ordering.
//     *
//     * @param loaded Source result set that is to be compared with target.
//     * @throws ComparisonException sdf
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
