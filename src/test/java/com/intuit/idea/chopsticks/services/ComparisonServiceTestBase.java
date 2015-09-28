package com.intuit.idea.chopsticks.services;

import com.mockrunner.mock.jdbc.MockResultSet;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeTest;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class ComparisonServiceTestBase {
    MockResultSet sMockRsData;
    MockResultSet tMockRsData;
    List<String> sPk;
    List<String> tPk;
    MockResultSet sMd;
    MockResultSet tMd;

    @BeforeTest
    public void setUp() throws Exception {
        sMockRsData = new MockResultSet("sourceResultSetMock");
        sMockRsData.addColumn("employeeId", new Integer[]{1, 2, 2});
        sMockRsData.addColumn("companyId", new Integer[]{1, 2, 3});
        sMockRsData.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        sMockRsData.addColumn("lastName", new String[]{"Chau", "Tian", "Chen"});
        sMockRsData.addColumn("lsdName", new String[]{"Chau", "Tian", "Chen"});
        sMockRsData.addColumn("createDate", new DateTime[]{new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0)});
        tMockRsData = new MockResultSet("targetResultSetMock");
        tMockRsData.addColumn("employeeId", new Integer[]{1, 2, 2});
        tMockRsData.addColumn("companyId", new Integer[]{1, 2, 3});
        tMockRsData.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        tMockRsData.addColumn("lastName", new String[]{"Chau", "Tian", "Chen"});
        tMockRsData.addColumn("createDate", new DateTime[]{new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0),
                new DateTime(2012, 2, 3, 0, 0, 0, 0)});
        sPk = asList("employeeId", "firstName");
        tPk = asList("employeeId", "firstName");
        sMd = new MockResultSet("sourceMetadataResultSetMock");
        sMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId", "firstName", "lsdName", "lastName", "createDate"});
        sMd.addColumn("DATA_TYPE", new Integer[]{4, 4, 12, 12, 12, 91});
        tMd = new MockResultSet("targetMetadataResultSetMock");
        tMd.addColumn("COLUMN_NAME", new String[]{"employeeId", "companyId", "firstName", "lastName", "createDate"});
        tMd.addColumn("DATA_TYPE", new Integer[]{4, 4, 12, 12, 91});
    }
}