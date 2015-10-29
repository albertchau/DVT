package com.intuit.idea.ziplock.services;

import com.intuit.idea.ziplock.utils.containers.Metadata;
import com.mockrunner.mock.jdbc.MockResultSet;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
@Test(groups = "comparison")
public class ComparisonTestBase {
    protected MockResultSet tarData;
    protected MockResultSet srcData;
    protected List<Metadata> tarMetadata;
    protected List<Metadata> srcMetadata;
    protected ComparisonService comparisonService;

    @BeforeMethod
    public void setup() {
        srcData = new MockResultSet("sourceResultSetMock");
        srcData.addColumn("employeeId", new Integer[]{2, 2, 2});
        srcData.addColumn("companyId", new Integer[]{1, 2, 34});
        srcData.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        srcData.addColumn("lastName", new String[]{"Chaqu", "Tiaen", "Chwen"});
        srcData.addColumn("createDate", new Date[]{new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis())});
//        int sIter = 1;
//        MockResultSetMetaData srcMd = new MockResultSetMetaData();
//        srcMd.setColumnLabel(sIter, "employeeId");
//        srcMd.setColumnType(sIter++, 4);
//        srcMd.setColumnLabel(sIter, "companyId");
//        srcMd.setColumnType(sIter++, 4);
//        srcMd.setColumnLabel(sIter, "firstName");
//        srcMd.setColumnType(sIter++, 12);
//        srcMd.setColumnLabel(sIter, "lastName");
//        srcMd.setColumnType(sIter++, 12);
//        srcMd.setColumnLabel(sIter, "createDate");
//        srcMd.setColumnType(sIter, 91);
//        srcMd.setColumnCount(5);
//        srcData.setResultSetMetaData(srcMd);
        tarData = new MockResultSet("targetResultSetMock");
        tarData.addColumn("employeeId", new Integer[]{2, 2, 2});
        tarData.addColumn("companyId", new Integer[]{1, 2, 3});
        tarData.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        tarData.addColumn("lastName", new String[]{"Chau", "Tian", "Chasen"});
        tarData.addColumn("createDate", new Date[]{new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis())});
//        int tIter = 1;
//        MockResultSetMetaData tarMd = new MockResultSetMetaData();
//        tarMd.setColumnLabel(tIter, "employeeId");
//        tarMd.setColumnType(tIter++, 4);
//        tarMd.setColumnLabel(tIter, "companyId");
//        tarMd.setColumnType(tIter++, 4);
//        tarMd.setColumnLabel(tIter, "firstName");
//        tarMd.setColumnType(tIter++, 12);
//        tarMd.setColumnLabel(tIter, "lastName");
//        tarMd.setColumnType(tIter++, 12);
//        tarMd.setColumnLabel(tIter, "createDate");
//        tarMd.setColumnType(tIter, 91);
//        tarMd.setColumnCount(5);
//        tarData.setResultSetMetaData(tarMd);
        srcMetadata = Arrays.asList(Metadata.createWithNoAliasing("employeeId", true, Integer.class),
                Metadata.createWithNoAliasing("companyId", false, Integer.class),
                Metadata.createWithNoAliasing("lastName", false, String.class),
                Metadata.createWithNoAliasing("firstName", true, String.class),
                Metadata.createWithNoAliasing("createDate", false, Date.class));
        tarMetadata = Arrays.asList(Metadata.createWithNoAliasing("employeeId", true, Integer.class),
                Metadata.createWithNoAliasing("companyId", false, Integer.class),
                Metadata.createWithNoAliasing("lastName", false, String.class),
                Metadata.createWithNoAliasing("firstName", true, String.class),
                Metadata.createWithNoAliasing("createDate", false, Date.class));
    }
}
