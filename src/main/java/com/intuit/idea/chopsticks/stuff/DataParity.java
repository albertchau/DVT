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

    public DataParity(Config testConfig) {

    }
}
