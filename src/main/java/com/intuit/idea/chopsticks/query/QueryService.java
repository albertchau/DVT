package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.utils.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.QueryCreationError;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public interface QueryService {

    String createDataQuery(List<Metadata> metadatas) throws QueryCreationError;

    String createExistenceQuery(List<Metadata> metadatas) throws QueryCreationError;

    String createCountQuery() throws QueryCreationError;

    String createDataQueryWithInputSamples(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) throws QueryCreationError;

    String createExistenceQueryWithInputSamples(List<Metadata> metadatas, Map<String, List<String>> pksWithHeaders) throws QueryCreationError;

    String getDateRange(String dateColumn, DateTime startDate, DateTime endDate);
}
