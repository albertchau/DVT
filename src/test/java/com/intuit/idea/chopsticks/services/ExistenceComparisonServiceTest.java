package com.intuit.idea.chopsticks.services;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class ExistenceComparisonServiceTest {

    @Test
    public void testCompare() throws Exception {
        MockResultSet srcRs = new MockResultSet("sourceResultSetMock");
        srcRs.addColumn("employeeId", new Integer[]{1, 2, 2, 50, 40, 30});
        srcRs.addColumn("companyId", new Integer[]{1, 2, 3, 333, 32, 5});

        MockResultSet tarRs = new MockResultSet("targetResultSetMock");
        tarRs.addColumn("employeeId", new Integer[]{1, 2, 2, 30, 40, 50});
        tarRs.addColumn("companyId", new Integer[]{1, 2, 3, 5, 32, 333});

        ExistenceComparisonService existenceComparisonService = new ExistenceComparisonService(null);
        List<String> sPk = Arrays.asList("employeeId", "companyId");
        List<String> tPk = Arrays.asList("employeeId", "companyId");
        MockResultSet sMd = new MockResultSet("sourceMetadataResultSetMock");
        sMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId"});
        sMd.addColumn("DATA_TYPE", new Integer[]{4, 4});
        MockResultSet tMd = new MockResultSet("targetMetadataResultSetMock");
        tMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId"});
        tMd.addColumn("DATA_TYPE", new Integer[]{4, 4});
        existenceComparisonService.existenceCompare(srcRs, tarRs, sPk, tPk, sMd, tMd);
//        existenceComparisonService.existenceCompare(srcRs, tarRs);

    }
}