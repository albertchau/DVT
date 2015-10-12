//package com.intuit.idea.chopsticks.services;
//
//import com.intuit.idea.chopsticks.providers.DataProvider;
//import com.intuit.idea.chopsticks.results.ResultStore;
//import com.intuit.idea.chopsticks.services.strategy.*;
//import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
//import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
//
//import java.util.*;
//
///**
// * Copyright 2015
// * todo deal with random sampling
// * @author albert
// */
//public abstract class ComparisonServiceBase implements ComparisonService {
//    protected final Set<ResultStore> resultStores;
//    protected final Loader loader;
//    protected final Extracter extracter;
//    protected final Comparer comparer;
//
//    /**
//     * Constructor with injected Result Stores
//     * @param resultStores to store the results
//     */
//    public ComparisonServiceBase(Set<ResultStore> resultStores, Loader loader, Extracter extracter, Comparer comparer) {
//        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
//        this.loader = loader == null ? new DefaultLoader(): loader;
//        this.extracter = extracter == null ? new DefaultExtracter(): extracter;
//        this.comparer = comparer == null ? new DefaultComparer(): comparer;
//    }
//
//    protected void closeConnections(DataProvider source, DataProvider target) {
//        source.closeConnections();
//        target.closeConnections();
//    }
//
//    protected void initializeConnections(DataProvider source, DataProvider target) throws ComparisonException {
//        try {
//            source.openConnections();
//        } catch (DataProviderException e) {
//            e.printStackTrace();
//            throw new ComparisonException("Could not establish connections to source.");
//        }
//        try {
//            target.openConnections();
//        } catch (DataProviderException e) {
//            e.printStackTrace();
//            throw new ComparisonException("Could not establish connections to target.");
//        }
//    }
//}
