package com.intuit.idea.chopsticks.utils.adapters;

import com.intuit.idea.chopsticks.utils.ResultSetIterator;
import com.intuit.idea.chopsticks.utils.Tuple;

import java.sql.ResultSet;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/15/15
 * ************************************
 */
public class ResultSetsAdapter {

    public static Stream<Tuple> convert(ResultSet resultSets) {
        ResultSetIterator resultSetIterator = new ResultSetIterator(resultSets);
        Iterable<Tuple> iterable = () -> resultSetIterator;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}
