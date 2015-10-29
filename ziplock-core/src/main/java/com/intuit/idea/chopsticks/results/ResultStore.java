package com.intuit.idea.chopsticks.results;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface ResultStore {

    void storeRowResults(List<ColumnComparisonResult> columnResults);

}
