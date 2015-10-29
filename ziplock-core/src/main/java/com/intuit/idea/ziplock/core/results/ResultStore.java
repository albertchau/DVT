package com.intuit.idea.ziplock.core.results;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface ResultStore {

    void storeRowResults(List<ColumnComparisonResult> columnResults);

}
