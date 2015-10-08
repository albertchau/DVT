package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.mockrunner.mock.jdbc.MockResultSet;
import com.mockrunner.mock.jdbc.MockResultSetMetaData;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

@Test(groups = "comparison")
public class DataComparisonServiceTest {
    private List<String> tPk;
    private List<String> sPk;
    private MockResultSetMetaData tarMd;
    private MockResultSet tarRs;
    private MockResultSetMetaData srcMd;
    private MockResultSet srcRs;

    @BeforeMethod
    public void setup() {
        srcRs = new MockResultSet("sourceResultSetMock");
        srcRs.addColumn("employeeId", new Integer[]{1, 2, 2});
        srcRs.addColumn("companyId", new Integer[]{1, 2, 34});
        srcRs.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        srcRs.addColumn("lastName", new String[]{"Chaqu", "Tiaen", "Chwen"});
        srcRs.addColumn("createDate", new Date[]{new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis())});
        int sIter = 1;
        srcMd = new MockResultSetMetaData();
        srcMd.setColumnLabel(sIter, "employeeId");
        srcMd.setColumnType(sIter++, 4);
        srcMd.setColumnLabel(sIter, "companyId");
        srcMd.setColumnType(sIter++, 4);
        srcMd.setColumnLabel(sIter, "firstName");
        srcMd.setColumnType(sIter++, 12);
        srcMd.setColumnLabel(sIter, "lastName");
        srcMd.setColumnType(sIter++, 12);
        srcMd.setColumnLabel(sIter, "createDate");
        srcMd.setColumnType(sIter, 91);
        srcMd.setColumnCount(5);
        srcRs.setResultSetMetaData(srcMd);

        tarRs = new MockResultSet("targetResultSetMock");
        tarRs.addColumn("employeeId", new Integer[]{1, 2, 2});
        tarRs.addColumn("companyId", new Integer[]{1, 2, 3});
        tarRs.addColumn("firstName", new String[]{"Albert", "Stefan", "Henry"});
        tarRs.addColumn("lastName", new String[]{"Chau", "Tian", "Chen"});
        tarRs.addColumn("createDate", new Date[]{new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis()),
                new Date(new DateTime(2012, 2, 3, 0, 0, 0, 0).getMillis())});
        int tIter = 1;
        tarMd = new MockResultSetMetaData();
        tarMd.setColumnLabel(tIter, "employeeId");
        tarMd.setColumnType(tIter++, 4);
        tarMd.setColumnLabel(tIter, "companyId");
        tarMd.setColumnType(tIter++, 4);
        tarMd.setColumnLabel(tIter, "firstName");
        tarMd.setColumnType(tIter++, 12);
        tarMd.setColumnLabel(tIter, "lastName");
        tarMd.setColumnType(tIter++, 12);
        tarMd.setColumnLabel(tIter, "createDate");
        tarMd.setColumnType(tIter, 91);
        tarMd.setColumnCount(5);
        tarRs.setResultSetMetaData(tarMd);

        sPk = Arrays.asList("employeeId", "firstName");
        tPk = Arrays.asList("employeeId", "firstName");
    }

    @Test
    public void testCompare() throws Exception {
        DataComparisonService dataComparisonService = new DataComparisonService(null);
        List<Metadata> srcMetadata = Arrays.asList(ComparisonUtils.extractSpecifiedMetadata(srcRs, null, sPk));
        List<Metadata> tarMetadata = Arrays.asList(ComparisonUtils.extractSpecifiedMetadata(tarRs, null, tPk));
        dataComparisonService.startComparison(srcRs, srcMetadata, tarRs, tarMetadata);
    }
}