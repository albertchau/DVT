package com.intuit.idea.chopsticks.stuff.reporters;

import java.util.Objects;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class SimpleResults <E> {
    public String colName;
    public E sVal, tVal;
    public Boolean result;

    public SimpleResults(String colName, E sVal, E tVal, Boolean result) {
        this.colName = colName;
        this.sVal = sVal;
        this.tVal = tVal;
        this.result = result;
    }

    public Class type() {
        return sVal.getClass();
    }


}
