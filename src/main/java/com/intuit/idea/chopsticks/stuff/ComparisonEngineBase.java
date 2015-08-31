package com.intuit.idea.chopsticks.stuff;

import com.intuit.idea.chopsticks.stuff.reporters.Reporter;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public abstract class ComparisonEngineBase implements ComparisonEngine {

    List<Reporter> reporters;

    public ComparisonEngineBase(List<Reporter> reporters) {
        this.reporters = reporters;
    }

    public final void compare(DataProvider source, DataProvider target) {
        initialize();
        compareImplementation(source, target);
        finish();
    }

    public void report(List<Boolean> outcomes, List sources, List targets) {
        reporters.forEach(r -> r.report(outcomes, sources, targets));
    }

    protected abstract void compareImplementation(DataProvider source, DataProvider target);

    protected void initialize() {
        reporters.forEach(Reporter::initialize);
    }

    protected void finish() {
        reporters.forEach(Reporter::finish);
    }

}
