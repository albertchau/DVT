package com.intuit.idea.ziplock.services.strategies.loaders;

import com.intuit.idea.ziplock.providers.DataProvider;
import com.intuit.idea.ziplock.services.ComparisonType;
import com.intuit.idea.ziplock.utils.containers.Loaded;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Loader {
    Loaded load(DataProvider source, DataProvider target, ComparisonType type) throws ComparisonException;
}
