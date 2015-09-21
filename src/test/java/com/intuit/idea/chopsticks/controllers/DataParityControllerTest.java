package com.intuit.idea.chopsticks.controllers;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.providers.StructuredJdbcDataProvider;
import com.intuit.idea.chopsticks.providers.VendorType;
import com.intuit.idea.chopsticks.query.QueryService;
import com.intuit.idea.chopsticks.query.QueryServiceBuilder;
import com.intuit.idea.chopsticks.query.TestType;
import com.intuit.idea.chopsticks.services.ComparisonService;
import com.intuit.idea.chopsticks.services.DataComparisonService;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.Timestamp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/13/15
 * ************************************
 */
public class DataParityControllerTest {

    private long start;
    private long end;

    @BeforeTest
    public void before() {
        start = System.nanoTime();
    }

    @Test
    public void mainTest() {
        //use injectors here...
        QueryService sqf = new QueryServiceBuilder().build("", VendorType.MYSQL, null, TestType.FULL);
        DataProvider source = new StructuredJdbcDataProvider(
                VendorType.MYSQL,
                "host",
                "port",
                "jdbc:mysql://localhost:3306/test",
                "root",
                "admin",
                "",
                "",
                "employees",
                null,
                sqf
        );
        QueryService tqf = new QueryServiceBuilder().build("", VendorType.MYSQL, null, TestType.FULL);
        DataProvider target = new StructuredJdbcDataProvider(
                VendorType.MYSQL,
                "host",
                "port",
                "jdbc:mysql://localhost:3306/test",
                "root",
                "admin",
                "",
                "",
                "employees",
                null,
                tqf
        );
        DataParityController dpc = new DataParityController(source, target);
        ComparisonService dataComparisonService = new DataComparisonService();
        dpc.registerComparisonService(dataComparisonService);
        dpc.run();
    }

    @Test
    public void testName() throws Exception {
        Timestamp timestamp = new Timestamp(271391);
        Timestamp timestamp2 = new Timestamp(271391);
        assertEquals(timestamp, timestamp2);
    }

    @Test
    public void testName2() throws Exception {
        Timestamp timestamp = new Timestamp(271391);
        Timestamp timestamp2 = new Timestamp(2271391);
        assertNotEquals(timestamp, timestamp2);
    }

    @AfterTest
    public void after() {
        end = System.nanoTime();

        System.out.println("start - end /100000 = " + ((end - start) / 1000000));
    }
}