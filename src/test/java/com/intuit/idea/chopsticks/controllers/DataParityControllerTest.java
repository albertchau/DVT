package com.intuit.idea.chopsticks.controllers;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.providers.StructuredJdbcDataProvider;
import com.intuit.idea.chopsticks.providers.VendorType;
import com.intuit.idea.chopsticks.query.QueryService;
import com.intuit.idea.chopsticks.query.QueryServiceBuilder;
import com.intuit.idea.chopsticks.query.TestType;
import com.intuit.idea.chopsticks.services.ComparisonServices;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.Assert.assertNotEquals;
import static org.testng.Assert.assertEquals;

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
    public void mainTest() throws SQLException, DataProviderException {
        //use injectors here...
        QueryService sqf = new QueryServiceBuilder().build("", VendorType.MYSQL, TestType.FULL);
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
        QueryService tqf = new QueryServiceBuilder().build("", VendorType.MYSQL, TestType.FULL);
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
        source.getVendorType();
        DataParityController dpc = new DataParityController(source, target);
//        ComparisonService dataComparisonService = new DataComparisonService(null, null, null, null);
        source.getData(ComparisonServices.DATA, null);
        source.getDataProviderType();
//        dpc.registerComparisonService(dataComparisonService);
        dpc.run();
    }

    @Test
    public void testMySqlEtE() throws Exception {
        QueryService sqf = new QueryServiceBuilder().build("employees", VendorType.MYSQL, TestType.FULL);
        StructuredJdbcDataProvider source = new StructuredJdbcDataProvider(
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

        QueryService tqf = new QueryServiceBuilder().build("employees", VendorType.MYSQL, TestType.FULL);
        StructuredJdbcDataProvider target = new StructuredJdbcDataProvider(
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

        target.openConnections();
        ResultSet sData = target.getData("Select * from test.employees where EmployeeID = 1001");
        ResultSet tData = target.getData("Select * from test.employees where EmployeeID = 1002");
        target.closeConnections();

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