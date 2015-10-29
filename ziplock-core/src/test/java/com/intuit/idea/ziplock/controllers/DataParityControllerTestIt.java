package com.intuit.idea.ziplock.controllers;

import com.intuit.idea.ziplock.providers.StructuredJdbcDataProvider;
import com.intuit.idea.ziplock.providers.VendorType;
import com.intuit.idea.ziplock.query.QueryService;
import com.intuit.idea.ziplock.query.QueryServiceBuilder;
import com.intuit.idea.ziplock.query.TestType;
import com.intuit.idea.ziplock.services.ComparisonService;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * TODO - move to Integration Test
 * ************************************
 * Author: achau1
 * Created On: 9/13/15
 * ************************************
 */
public class DataParityControllerTestIt {

    public static final String TEST_RESOURCE_PATH = "src/test/resources/";
    public static final String DB_URL = "jdbc:mysql://localhost/test";
    private long start;
    private String USER = "root";
    private String PASS = "admin";
    private Connection conn = null;
    private Statement stmt = null;

    public static Path getFile(String resourceFile) {
        return new File(TEST_RESOURCE_PATH + resourceFile).toPath();
    }

    @BeforeTest
    public void setup() throws SQLException, IOException {
        createAndPopulateTable("students_a_insert.sql", "students_a");
        createAndPopulateTable("students_b_insert.sql", "students_b");
        start = System.nanoTime();
    }

    @Test
    public void testDbLoad() throws Exception {
        String tableName = "students_a";
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 10");
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            Function<Integer, String> getColumn = i -> {
                try {
                    return resultSet.getString(i);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            };
            String row = IntStream.range(1, columnCount + 1)
                    .boxed()
                    .map(getColumn)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(", "));
            System.out.println("row = " + row);
        }
        stmt.close();
        conn.close();
    }

    @Test
    public void testMySqlEtE() throws Exception {
        QueryService sourceQueryService = new QueryServiceBuilder().build("students_a", VendorType.MYSQL, TestType.FULL);
        StructuredJdbcDataProvider source = new StructuredJdbcDataProvider(
                VendorType.MYSQL,
                "host",
                "port",
                "jdbc:mysql://localhost:3306/test",
                "root",
                "admin",
                "test",
                "hivePrincipal",
                "students_a",
                null,
                sourceQueryService
        );
        QueryService targetQueryService = new QueryServiceBuilder().build("students_b", VendorType.MYSQL, TestType.FULL);
        StructuredJdbcDataProvider target = new StructuredJdbcDataProvider(
                VendorType.MYSQL,
                "host",
                "port",
                "jdbc:mysql://localhost:3306/test",
                "root",
                "admin",
                "test",
                "hivePrincipal",
                "students_b",
                null,
                targetQueryService
        );
        List<ComparisonService> allComparisons = ComparisonService.createAllComparisons(null);
        DataParityController dataParityController =
                new DataParityController(source, target, allComparisons);
        dataParityController.run();
    }

    @AfterTest
    public void after() throws SQLException {
        long end = System.nanoTime();
        destroyTable("students_a");
        destroyTable("students_b");
        System.out.println("start - end /100000 = " + ((end - start) / 1000000));
    }

    public void createAndPopulateTable(String resourceFile, String tableName) throws SQLException, IOException {
        String tableDropQuery = "DROP TABLE IF EXISTS " + tableName;
        String createTableSql = "create table " + tableName + "(" +
                "id INT," +
                "first_name VARCHAR(50)," +
                "last_name VARCHAR(50)," +
                "email VARCHAR(50)," +
                "country VARCHAR(50)," +
                "create_date DATE," +
                "PRIMARY KEY (id)" +
                ");";
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
        stmt.executeUpdate(tableDropQuery);
        stmt.executeUpdate(createTableSql);
        Files.lines(getFile(resourceFile))
                .forEach(s -> {
                    try {
                        stmt.executeUpdate(s);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
        stmt.close();
        conn.close();
    }

    public void destroyTable(String tableName) throws SQLException {
        String tableDropQuery = "DROP TABLE IF EXISTS " + tableName;
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
        stmt.executeUpdate(tableDropQuery);
        stmt.close();
        conn.close();
    }
}