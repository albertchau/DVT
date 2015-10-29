package com.intuit.idea.ziplock.services.strategies.extractors;

import com.intuit.idea.ziplock.utils.containers.Extracted;
import com.intuit.idea.ziplock.utils.containers.Loaded;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Extractor {
    Extracted extract(Loaded loaded) throws ComparisonException;
}
