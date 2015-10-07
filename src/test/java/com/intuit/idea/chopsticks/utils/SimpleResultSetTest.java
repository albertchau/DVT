package com.intuit.idea.chopsticks.utils;

import com.intuit.idea.chopsticks.services.CountComparisonService;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class SimpleResultSetTest {

    SimpleResultSet srs;

    @BeforeMethod
    public void setUp() throws Exception {
        List<List<Object>> sampleCounts = IntStream.range(0, 11)
                .boxed()
                .map(i -> Collections.singletonList((Object) i))
                .collect(Collectors.toList());
        srs = new SimpleResultSet(sampleCounts);
    }

    @Test
    public void testGettingCounts() throws Exception {
        List<Integer> integers = CountComparisonService.resultSetToList(srs);
        int sum = integers.stream().mapToInt(Integer::intValue).sum();
        Assert.assertEquals(sum, 55, "Did not sum correctly");
    }

    @AfterMethod
    public void tearDown() throws Exception {
        srs = null;
    }

}