package com.intuit.idea.chopsticks.stuff;

import com.typesafe.config.Config;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public final class DataParity {
    DataProvider source;

    DataProvider target;

    List<ComparisonEngine> comparisonEngines;

    public DataParity(DataProvider source, DataProvider target, List<ComparisonEngine> comparisonEngines) {
        this.source = source;
        this.target = target;
        this.comparisonEngines = comparisonEngines;
    }


}
