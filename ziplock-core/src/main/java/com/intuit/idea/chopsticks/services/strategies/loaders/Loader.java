package com.intuit.idea.chopsticks.services.strategies.loaders;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.services.ComparisonType;
import com.intuit.idea.chopsticks.utils.containers.Loaded;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Loader {
    Loaded load(DataProvider source, DataProvider target, ComparisonType type) throws ComparisonException;
}
