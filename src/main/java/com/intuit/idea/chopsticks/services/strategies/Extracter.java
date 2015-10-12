package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.services.transforms.Extracted;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Extracter {
    Extracted extract(Loaded loaded) throws ComparisonException;
}
