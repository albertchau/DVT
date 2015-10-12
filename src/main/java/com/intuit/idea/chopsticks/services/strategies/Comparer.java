package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.services.transforms.Extracted;

import java.util.Set;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Comparer {
    void compare(final Extracted extracted, final Set<ResultStore> resultStores);
}
