package com.intuit.idea.chopsticks.services;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.testng.annotations.Test;

/**
 * Copyright 2015
 *
 * @author albert
 */
@Test(groups = "comparison")
public class CountComparisonServiceTest {

    @Test
    public void testCompare() throws Exception {
        MockResultSet srcRs = new MockResultSet("sourceResultSetMock");
        srcRs.addColumn("count", new Integer[]{1, 2, 2});
        MockResultSet tarRs = new MockResultSet("targetResultSetMock");
        tarRs.addColumn("count", new Integer[]{5});
        CountComparisonService countComparisonService = new CountComparisonService(null, 0.0);
        countComparisonService.countCompare(srcRs, tarRs);
    }
}