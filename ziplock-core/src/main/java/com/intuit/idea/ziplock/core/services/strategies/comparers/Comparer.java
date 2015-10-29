package com.intuit.idea.ziplock.core.services.strategies.comparers;

import com.intuit.idea.ziplock.core.results.ResultStore;
import com.intuit.idea.ziplock.core.utils.containers.Extracted;
import com.intuit.idea.ziplock.core.utils.exceptions.ComparisonException;

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
