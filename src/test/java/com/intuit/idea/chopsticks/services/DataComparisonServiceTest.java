package com.intuit.idea.chopsticks.services;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class DataComparisonServiceTest {

    @Test
    public void testCompare() throws Exception {
        MockResultSet srcRs = new MockResultSet("sourceResultSetMock");
        srcRs.addColumn("employeeId", new Integer[]{1, 2, 2});
        srcRs.addColumn("companyId", new Integer[]{1, 2, 3});
        srcRs.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        srcRs.addColumn("lastName", new String[]{"Chau", "Tian", "Chen"});
        srcRs.addColumn("lsdName", new String[]{"Chau", "Tian", "Chen"});
        srcRs.addColumn("createDate", new DateTime[]{new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0)});

        MockResultSet tarRs = new MockResultSet("targetResultSetMock");
        tarRs.addColumn("employeeId", new Integer[]{1, 2, 2});
        tarRs.addColumn("companyId", new Integer[]{1, 2, 3});
        tarRs.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        tarRs.addColumn("lastName", new String[]{"Chau", "Tian", "Chen"});
        tarRs.addColumn("createDate", new DateTime[]{new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0)});

        DataComparisonService dataComparisonService = new DataComparisonService(null);
        List<String> sPk = Arrays.asList("employeeId", "firstName");
        List<String> tPk = Arrays.asList("employeeId", "firstName");
        MockResultSet sMd = new MockResultSet("sourceMetadataResultSetMock");
        sMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId", "firstName", "lsdName", "lastName", "createDate"});
        sMd.addColumn("DATA_TYPE", new Integer[]{4, 4, 12, 12, 12, 91});
        MockResultSet tMd = new MockResultSet("targetMetadataResultSetMock");
        tMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId", "firstName", "lastName", "createDate"});
        tMd.addColumn("DATA_TYPE", new Integer[]{4, 4, 12, 12, 91});
        dataComparisonService.dataCompare(srcRs, tarRs, sPk, tPk, sMd, tMd);
    }
}