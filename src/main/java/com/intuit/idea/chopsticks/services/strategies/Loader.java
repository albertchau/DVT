package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.services.ComparisonServices;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Loader {
    Loaded load(DataProvider source, DataProvider target, ComparisonServices type) throws ComparisonException;
}
