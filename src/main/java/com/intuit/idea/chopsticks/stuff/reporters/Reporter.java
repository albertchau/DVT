package com.intuit.idea.chopsticks.stuff.reporters;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Reporter {

    void report(List<Boolean> outcomes, List sources, List targets);

    void initialize();

    void finish();

    void generateReport();
}
