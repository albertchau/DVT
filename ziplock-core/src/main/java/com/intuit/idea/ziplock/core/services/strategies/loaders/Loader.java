package com.intuit.idea.ziplock.core.services.strategies.loaders;

import com.intuit.idea.ziplock.core.providers.DataProvider;
import com.intuit.idea.ziplock.core.services.ComparisonType;
import com.intuit.idea.ziplock.core.utils.containers.Loaded;
import com.intuit.idea.ziplock.core.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Loader {
    Loaded load(DataProvider source, DataProvider target, ComparisonType type) throws ComparisonException;
}
