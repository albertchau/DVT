package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.*;
import static com.intuit.idea.chopsticks.services.ComparisonUtils.*;

/**
 * Copyright 2015
 * todo deal with random sampling
 * @author albert
 */
public abstract class ComparisonServiceBase implements ComparisonService {
    private static final Logger logger = LoggerFactory.getLogger(ComparisonServiceBase.class);
    protected final Set<ResultStore> resultStores;

    /**
     * Constructor with injected Result Stores
     *
     * @param resultStores to store the results
     */
    public ComparisonServiceBase(Set<ResultStore> resultStores) {
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
    }

    // todo - extract out strategy pattern for execution
    @SuppressWarnings("unchecked")
    protected void mergeCompareSortedRows(List<Comparable[]> sRowList, List<Comparable[]> tRowList, CombinedMetadata[] metadatas) {
        BiFunction[] comparers = Arrays.stream(metadatas)
                .map(CombinedMetadata::getComparer)
                .toArray(BiFunction[]::new);
        int numOfPks = (int) Arrays.stream(metadatas)
                .filter(CombinedMetadata::isPk)
                .count();
        int numOfColumns = metadatas.length;
        Iterator<Comparable[]> sRowIter = sRowList.iterator();
        Iterator<Comparable[]> tRowIter = tRowList.iterator();
        Comparable[] sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        Comparable[] tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        while (sRow != null && tRow != null) {
            Integer compareInt = compareColumns(sRow, tRow, comparers, 0, numOfPks);
            if (compareInt < 0) {
                onlyInSource(sRow, metadatas);
                sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                continue;
            }
            if (compareInt > 0) {
                onlyInTarget(tRow, metadatas);
                tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                continue;
            }
            List<ColumnComparisonResult> results = new ArrayList<>();
            for (int i = 0; i < numOfColumns; i++) {
                if (metadatas[i].isPk()) {
                    results.add(createMates(true, metadatas[i], sRow[i], tRow[i], true));
                    continue;
                }
                boolean isEqual = metadatas[i].getComparer().apply(sRow[i], tRow[i]) == 0;
                results.add(createMates(isEqual, metadatas[i], sRow[i], tRow[i], false));
            }
            String resultString = results.stream()
                    .map(ColumnComparisonResult::getOutcome)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            System.out.println("resultString = " + resultString);
            resultStores.stream()
                    .forEach(rs -> rs.storeRowResults(this, results));
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }
        while (sRow != null) {
            onlyInSource(sRow, metadatas);
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        }
        while (tRow != null) {
            onlyInTarget(tRow, metadatas);
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }
    }

    // todo - extract out strategy pattern for setup/pulling data
    protected void comparisonStrategy(ResultSet srcRowSet, ResultSet tarRowSet, Metadata[] srcMetadata, Metadata[] tarMetadata) throws ComparisonException {
        List<Comparable[]> sRowList;
        List<Comparable[]> tRowList;
        CombinedMetadata[] metadatas = mergeMetadata(srcMetadata, tarMetadata);
        SetupContainer setupContainer = new SetupContainer(srcRowSet, tarRowSet, metadatas).invoke();
        sRowList = setupContainer.getsRowList();
        tRowList = setupContainer.gettRowList();
        mergeCompareSortedRows(sRowList, tRowList, metadatas);
    }

    protected void closeConnections(DataProvider source, DataProvider target) {
        source.closeConnections();
        target.closeConnections();
    }

    protected void initializeConnections(DataProvider source, DataProvider target) throws ComparisonException {
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

    /*
    Reports the result stores that there was something only found in the target.
     */
    protected void onlyInTarget(Comparable[] sRow, CombinedMetadata[] metadatas) {
        logger.warn("There are rows in target not found in source...");
        List<ColumnComparisonResult> tmp = IntStream.range(0, metadatas.length)
                .boxed()
                .map(i -> createOnlySource(metadatas[i], sRow[i]))
                .collect(Collectors.toList());
        resultStores.stream()
                .forEach(rs -> rs.storeRowResults(this, tmp));
    }

    /*
    Reports the result stores that there was something only found in the source.
     */
    protected void onlyInSource(Comparable[] sRow, CombinedMetadata[] metadatas) {
        logger.warn("There are rows in source not found in target...");
        List<ColumnComparisonResult> tmp = IntStream.range(0, metadatas.length)
                .boxed()
                .map(i -> createOnlyTarget(metadatas[i], sRow[i]))
                .collect(Collectors.toList());
        resultStores.stream()
                .forEach(rs -> rs.storeRowResults(this, tmp));
    }

    // example method object pattern
    private class SetupContainer {
        private ResultSet srcRowSet;
        private ResultSet tarRowSet;
        private CombinedMetadata[] metadatas;
        private List<Comparable[]> sRowList;
        private List<Comparable[]> tRowList;

        public SetupContainer(ResultSet srcRowSet, ResultSet tarRowSet, CombinedMetadata... metadatas) {
            this.srcRowSet = srcRowSet;
            this.tarRowSet = tarRowSet;
            this.metadatas = metadatas;
        }

        public List<Comparable[]> getsRowList() {
            return sRowList;
        }

        public List<Comparable[]> gettRowList() {
            return tRowList;
        }

        public SetupContainer invoke() throws ComparisonException {
            try {
                sRowList = resultSetToSortedList(srcRowSet, metadatas, CombinedMetadata::getSrc);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("During setup, retrieving source's resultsets into memory failed.");
                throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
            }
            try {
                tRowList = resultSetToSortedList(tarRowSet, metadatas, CombinedMetadata::getTar);
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("During setup, retrieving target's resultsets into memory failed.");
                throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
            }
            return this;
        }
    }
}
