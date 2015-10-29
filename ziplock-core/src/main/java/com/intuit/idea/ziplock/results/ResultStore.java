package com.intuit.idea.ziplock.results;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface ResultStore {

    void storeRowResults(List<ColumnComparisonResult> columnResults);

}
