package com.intuit.idea.ziplock.core.results;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class InMemoryResultStore implements ResultStore, Reportable {

    private final static Logger logger = LoggerFactory.getLogger(InMemoryResultStore.class);

    private List<List<ColumnComparisonResult>> results;

    public InMemoryResultStore() {
        results = new ArrayList<>();
    }

    @Override
    public void storeRowResults(List<ColumnComparisonResult> columnResults) {
        results.add(columnResults);
    }

    @Override
    public void report() {
        double totalRows = results.size() * 1.0;
        long countRowPass = results.stream()
                .filter(row -> row.stream().allMatch(ColumnComparisonResult::getOutcome))
                .count();
        logger.info("Overall row quality: " + countRowPass / totalRows);
        logger.info("Rows passed: " + countRowPass);
        long rowsInSrcOnly = results.stream()
                .filter(row -> !row.stream().anyMatch(col -> col.gettVal() != null))
                .count();
        logger.info("Rows found only in target: " + rowsInSrcOnly);
        long rowsInTarOnly = results.stream()
                .filter(row -> !row.stream().anyMatch(col -> col.getsVal() != null))
                .count();
        logger.info("Rows found only in source: " + rowsInTarOnly);
        double mismatchedRows = totalRows - countRowPass;
        logger.info("Rows mismatched: " + mismatchedRows);
        Map<String, Integer> colCountMap = new HashMap<>();
        results.stream()
                .forEach(row -> row.stream()
                        .forEach(col -> colCountMap
                                .merge(col.getField(),
                                        col.getOutcome() ? 1 : 0,
                                        (old, x) -> x + old)
                        )
                );
        colCountMap.forEach((s, i) -> logger.info(s + " quality: " + (i / mismatchedRows) + "; \tpassed: " + (i) + "; \tfailed: " + (mismatchedRows - i)));
    }
}
