package com.intuit.idea.chopsticks.query;

import org.joda.time.DateTime;

import java.util.List;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public interface QueryService {

    String createDataQuery();

    String createExistenceQuery();

    String createCountQuery();

    String createDataQuery(List<List<String>> pksToInclude, List<String> columns);

    String createExistenceQuery(List<List<String>> pksToInclude, List<String> columns);

    String getDateRange(String dateColumn, DateTime startDate, DateTime endDate);
}
