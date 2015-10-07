package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 * BiConsumer-esque
 * @author albert
 */
public interface ComparisonService {

    void compare(DataProvider source, DataProvider target) throws ComparisonException;

}
