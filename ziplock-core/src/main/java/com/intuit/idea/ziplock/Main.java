package com.intuit.idea.ziplock;

import com.intuit.idea.ziplock.controllers.DataParityController;
import com.intuit.idea.ziplock.providers.StructuredJdbcDataProvider;
import com.intuit.idea.ziplock.providers.VendorType;
import com.intuit.idea.ziplock.query.QueryService;
import com.intuit.idea.ziplock.query.QueryServiceBuilder;
import com.intuit.idea.ziplock.query.TestType;
import com.intuit.idea.ziplock.services.ComparisonService;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;

import java.io.IOException;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 * @link http://onoffswitch.net/simplifying-class-matching-java-8/
 */
public class Main {

    public static void main(String[] args) throws ComparisonException, IOException {
        runTestWithLoadedDatabaseAssumed();
    }

    private static void runTestWithLoadedDatabaseAssumed() throws ComparisonException {
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
}