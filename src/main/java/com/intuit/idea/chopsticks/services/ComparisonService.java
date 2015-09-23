package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ColumnComparisonResult;

import java.sql.SQLException;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface ComparisonService {

    //todo make private
    void report(List<ColumnComparisonResult> rowResults);

    void init();

    void compare(DataProvider source, DataProvider target) throws SQLException;

    void finish();
}
