package com.intuit.idea.ziplock.core.utils;

import java.util.function.DoubleBinaryOperator;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/28/15
 * ************************************
 */
public enum CompareMapping {
    STR_TO_INT("+", (l, r) -> l + r);

    private final String symbol;
    private final DoubleBinaryOperator binaryOperator;

    CompareMapping(final String symbol, final DoubleBinaryOperator binaryOperator) {
        this.symbol = symbol;
        this.binaryOperator = binaryOperator;
    }

    public String getSymbol() {
        return symbol;
    }

}