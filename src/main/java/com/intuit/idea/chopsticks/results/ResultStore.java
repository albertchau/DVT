package com.intuit.idea.chopsticks.results;

import com.intuit.idea.chopsticks.services.ComparisonService;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface ResultStore {

    void init();

    void stop();

    void storeRowResults(ComparisonService comparisonService, List<ColumnComparisonResult> columnResults);

}
