package com.intuit.idea.chopsticks.services.strategies.extractors;

import com.intuit.idea.chopsticks.utils.containers.Extracted;
import com.intuit.idea.chopsticks.utils.containers.Loaded;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Extractor {
    Extracted extract(Loaded loaded) throws ComparisonException;
}
