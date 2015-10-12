package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.services.transforms.Loaded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = "comparison")
public class DataComparisonServiceTest extends ComparisonTestBase {
    private static final Logger logger = LoggerFactory.getLogger(DataComparisonServiceTest.class);

    @BeforeMethod
    @Override
    public void setup() {
        super.setup();
        comparisonService = ComparisonService.createForData(null);
    }

    @Test
    public void testCompare() throws Exception {
        Loaded loaded = new Loaded(srcData, srcMetadata, tarData, tarMetadata);
        comparisonService.startComparison(loaded);
    }
}