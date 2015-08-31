package com.intuit.idea.chopsticks.stuff;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface ComparisonEngine {

    void compare(DataProvider source, DataProvider target);

    void report(List<Boolean> outcomes, List sources, List targets);

}
