package com.intuit.idea.chopsticks.query;

import com.intuit.idea.chopsticks.providers.VendorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MySqlQueryServiceTest {
    public static Logger logger = LoggerFactory.getLogger(MySqlQueryServiceTest.class);

    @DataProvider
    public Object[][] getQueryServices() {
        List<Metadata> metadatas = Arrays.asList(new Metadata("empId", true, Integer.class),
                new Metadata("empName", false, String.class));
        QueryService mqs = new QueryServiceBuilder()
                .build("employees", VendorType.MYSQL, metadatas, TestType.FULL);
        List<QueryService> qss = new ArrayList<>();

        qss.add(mqs);
        List<Object[]> collect = qss.stream().map(qs -> new Object[]{qs}).collect(Collectors.toList());
        return collect.toArray(new Object[][]{{}});
    }

    @Test(dataProvider = "getQueryServices")
    public void testCreateDataQuery(QueryService mqs) throws Exception {
        logger.info(mqs.createDataQuery());
    }

    @Test
    public void testCreateExistenceQuery() throws Exception {

    }

    @Test
    public void testCreateCountQuery() throws Exception {

    }

    @Test
    public void testCreateDataQuerySampled() throws Exception {

    }

    @Test
    public void testCreateExistenceQuerySampled() throws Exception {

    }

    @Test
    public void testGetDateRange() throws Exception {

    }
}