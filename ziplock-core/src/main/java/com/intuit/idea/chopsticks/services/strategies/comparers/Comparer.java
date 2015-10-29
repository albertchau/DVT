package com.intuit.idea.chopsticks.services.strategies.comparers;

import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.containers.Extracted;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;

import java.util.Set;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Comparer {

    void compare(final Extracted extracted, final Set<ResultStore> resultStores) throws ComparisonException;

    Boolean getResult();
}
