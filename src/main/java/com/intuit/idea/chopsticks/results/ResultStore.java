package com.intuit.idea.chopsticks.results;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface ResultStore {

    void init();

    void stop();

    void storeRowResults(List<ColumnComparisonResult> columnResults);

}
