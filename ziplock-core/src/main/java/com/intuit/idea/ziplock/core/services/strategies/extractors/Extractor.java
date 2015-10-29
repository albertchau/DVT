package com.intuit.idea.ziplock.core.services.strategies.extractors;

import com.intuit.idea.ziplock.core.utils.containers.Extracted;
import com.intuit.idea.ziplock.core.utils.containers.Loaded;
import com.intuit.idea.ziplock.core.utils.exceptions.ComparisonException;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface Extractor {
    Extracted extract(Loaded loaded) throws ComparisonException;
}
