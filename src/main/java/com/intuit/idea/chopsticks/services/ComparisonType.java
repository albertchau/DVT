package com.intuit.idea.chopsticks.services;

import static com.intuit.idea.chopsticks.utils.CollectionUtils.toProperCase;

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
