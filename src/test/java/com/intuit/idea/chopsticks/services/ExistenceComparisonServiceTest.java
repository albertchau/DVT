package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.services.transforms.Loaded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Copyright 2015
 *
 * @author albert
 */
@Test(groups = "comparison")
public class ExistenceComparisonServiceTest extends ComparisonTestBase {
    private static final Logger logger = LoggerFactory.getLogger(ExistenceComparisonServiceTest.class);

    @BeforeMethod
    @Override
    public void setup() {
        super.setup();
        comparisonService = ComparisonService.createForExistence(null);
    }

    @Test
    public void testCompareWithMetadata() throws Exception {
        logger.info("testCompareWithMetadata");
        Loaded loaded = new Loaded(srcData, srcMetadata, tarData, tarMetadata);
        comparisonService.startComparison(loaded);
    }

    @Test
    public void testCompareWithoutMetadata() throws Exception {
    }
}