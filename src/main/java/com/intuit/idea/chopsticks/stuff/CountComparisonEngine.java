package com.intuit.idea.chopsticks.stuff;

import com.intuit.idea.chopsticks.stuff.reporters.Reporter;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class CountComparisonEngine extends ComparisonEngineBase {

    public CountComparisonEngine(List<Reporter> reporters) {
        super(reporters);

    }

    @Override
    protected void compareImplementation(DataProvider source, DataProvider target) {

    }
}
