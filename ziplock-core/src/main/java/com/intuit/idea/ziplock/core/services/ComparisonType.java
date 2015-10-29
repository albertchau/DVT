package com.intuit.idea.ziplock.core.services;

import com.intuit.idea.ziplock.core.utils.CollectionUtils;

/**
 * Types of comparison services we offer. Metadata is not listed here.
 *
 * @author albert
 */
public enum ComparisonType {
    DATA,
    EXISTENCE,
    COUNT,
    METADATA;

    public String stringify() {
        return CollectionUtils.toProperCase(this.toString()) + " Comparison";
    }
}
