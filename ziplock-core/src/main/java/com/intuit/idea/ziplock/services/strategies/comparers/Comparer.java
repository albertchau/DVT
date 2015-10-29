package com.intuit.idea.ziplock.services.strategies.comparers;

import com.intuit.idea.ziplock.results.ResultStore;
import com.intuit.idea.ziplock.utils.containers.Extracted;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;

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
