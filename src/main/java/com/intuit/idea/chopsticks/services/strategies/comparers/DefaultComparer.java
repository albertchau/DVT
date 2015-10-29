package com.intuit.idea.chopsticks.services.strategies.comparers;

import com.intuit.idea.chopsticks.results.ColumnComparisonResult;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;
import com.intuit.idea.chopsticks.utils.containers.Extracted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.*;
import static com.intuit.idea.chopsticks.utils.ComparisonUtils.compareColumns;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DefaultComparer implements Comparer {
    private static Logger logger = LoggerFactory.getLogger(DefaultComparer.class);
    private Set<ResultStore> resultStores;
    private Boolean isPassed = true;

    @Override
    public void compare(Extracted extracted, final Set<ResultStore> resultStores) {
        logger.debug("Comparing data.");
        this.resultStores = resultStores;
        List<Comparable[]> sRowList = extracted.srcList;
        List<Comparable[]> tRowList = extracted.tarList;
        CombinedMetadata[] metadatas = extracted.metadatas;
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
                isPassed = false;
                onlyInSource(sRow, metadatas);
                sRow = sRowIter.hasNext() ? sRowIter.next() : null;
                continue;
            }
            if (compareInt > 0) {
                isPassed = false;
                onlyInTarget(tRow, metadatas);
                tRow = tRowIter.hasNext() ? tRowIter.next() : null;
                continue;
            }
            List<ColumnComparisonResult> results = new ArrayList<>();
            for (int i = 0; i < numOfColumns; i++) {
                if (metadatas[i].isPk()) {
                    results.add(createMatesFromMeta(true, metadatas[i], sRow[i], tRow[i]));
                    continue;
                }
                boolean isEqual = metadatas[i].getComparer().apply(sRow[i], tRow[i]) == 0;
                isPassed = isPassed && isEqual;
                results.add(createMatesFromMeta(isEqual, metadatas[i], sRow[i], tRow[i]));
            }
            resultStores.stream()
                    .forEach(rs -> rs.storeRowResults(results));
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }
        while (sRow != null) {
            isPassed = false;
            onlyInSource(sRow, metadatas);
            sRow = sRowIter.hasNext() ? sRowIter.next() : null;
        }
        while (tRow != null) {
            isPassed = false;
            onlyInTarget(tRow, metadatas);
            tRow = tRowIter.hasNext() ? tRowIter.next() : null;
        }
        logger.debug((isPassed ? "[PASSED]" : "[FAILED]") + " finished comparing data.");
    }

    @Override
    public Boolean getResult() {
        return isPassed;
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
                .forEach(rs -> rs.storeRowResults(tmp));
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
                .forEach(rs -> rs.storeRowResults(tmp));
    }

}
