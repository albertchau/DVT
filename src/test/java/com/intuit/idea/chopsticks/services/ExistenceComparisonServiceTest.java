package com.intuit.idea.chopsticks.services;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class ExistenceComparisonServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ExistenceComparisonServiceTest.class);
    Random r = new Random();
    private MockResultSet srcRs;
    private MockResultSet tarRs;
    private List<String> sPk;
    private List<String> tPk;
    private MockResultSet sMd;
    private MockResultSet tMd;

    @BeforeMethod
    public void setup() {
        sPk = Arrays.asList("employeeId", "companyId");
        tPk = Arrays.asList("employeeId", "companyId");

        String s = randStr();
        srcRs = new MockResultSet("sourceResultSetMock" + s);
        srcRs.addColumn("employeeId", new Integer[]{1, 2, 2, 50, 40, 30});
        srcRs.addColumn("companyId", new Integer[]{1, 2, 3, 333, 32, 6});

        tarRs = new MockResultSet("targetResultSetMock" + s);
        tarRs.addColumn("employeeId", new Integer[]{1, 2, 2, 30, 40, 50});
        tarRs.addColumn("companyId", new Integer[]{1, 2, 3, 5, 32, 333});

        sMd = new MockResultSet("sourceMetadataResultSetMock" + s);
        sMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId"});
        sMd.addColumn("DATA_TYPE", new Integer[]{4, 4});

        tMd = new MockResultSet("targetMetadataResultSetMock" + s);
        tMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId"});
        tMd.addColumn("DATA_TYPE", new Integer[]{4, 4});
    }

    public String randStr() {
        return String.valueOf(r.nextInt(1000));
    }

    @Test
    public void testCompareWithMetadata() throws Exception {
        logger.info("testCompareWithMetadata");
        ExistenceComparisonService existenceComparisonService = new ExistenceComparisonService(null);
//        existenceComparisonService.existenceCompare(srcRs, tarRs, sPk, tPk, sMd, tMd);
    }

    @Test
    public void testCompareWithoutMetadata() throws Exception {
        logger.info("testCompareWithoutMetadata");
        ExistenceComparisonService existenceComparisonService = new ExistenceComparisonService(null);
        existenceComparisonService.existenceCompare(srcRs, tarRs);
    }
}