package com.intuit.idea.ziplock.services;

import static com.intuit.idea.ziplock.utils.CollectionUtils.toProperCase;

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
        return toProperCase(this.toString()) + " Comparison";
    }
}
