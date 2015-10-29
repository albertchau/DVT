package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.utils.containers.Loaded;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

/**
 * Copyright 2015
 *
 * @author albert
 */
@Test(groups = "comparison")
public class CountComparisonServiceTest extends ComparisonTestBase  {

    @BeforeMethod
    @Override
    public void setup() {
        srcData = new MockResultSet("sourceResultSetMock");
        srcData.addColumn("count", new Integer[]{1, 2, 2});
        tarData = new MockResultSet("targetResultSetMock");
        tarData.addColumn("count", new Integer[]{5});
        srcMetadata = Collections.singletonList(Metadata.createWithNoAliasing("count", true, Integer.class));
        tarMetadata = Collections.singletonList(Metadata.createWithNoAliasing("count", true, Integer.class));
        try {
            comparisonService = ComparisonService.createForCount(null);
        } catch (ComparisonException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCompare() throws Exception {
        Loaded loaded = new Loaded(srcData, srcMetadata, tarData, tarMetadata);
        comparisonService.startComparison(loaded);
    }
}