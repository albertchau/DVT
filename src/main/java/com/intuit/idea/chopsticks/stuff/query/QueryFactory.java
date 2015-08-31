package com.intuit.idea.chopsticks.stuff.query;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface QueryFactory {

    String createDataQuery(List<String> columns,
                           List<String> primaryKeys,
                           DateTime start,
                           DateTime end,
                           String dateColumn,
                           Integer limit,
                           OrderDirection direction);
    /*

    cp sed ld
    cp sd ld
    cp ed ld
    cp sed
    cp sd
    cp ed

     */
}
