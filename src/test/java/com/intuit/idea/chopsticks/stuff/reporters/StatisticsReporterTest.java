package com.intuit.idea.chopsticks.stuff.reporters;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class StatisticsReporterTest {

    @Test
    public void testGenerateReport() throws Exception {
        List<String> headers = Arrays.asList("a", "b", "c");
        StatisticsReporter sr = new StatisticsReporter(headers);
        List<List<SimpleResults>> resultRows = Arrays.asList(
                Arrays.asList(
                        new SimpleResults<>("a", "", "", false),
                        new SimpleResults<>("b", "", "", true),
                        new SimpleResults<>("c", "", "", false)),
                Arrays.asList(
                        new SimpleResults<>("a", "", "", false),
                        new SimpleResults<>("b", "", "", false),
                        new SimpleResults<>("c", "", "", true)),
                Arrays.asList(
                        new SimpleResults<>("a", "", "", true),
                        new SimpleResults<>("b", "", "", true),
                        new SimpleResults<>("c", "", "", false)),
                Arrays.asList(
                        new SimpleResults<>("a", "", "", true),
                        new SimpleResults<>("b", "", "", true),
                        new SimpleResults<>("c", "", "", false)),
                Arrays.asList(
                        new SimpleResults<>("a", "", "", true),
                        new SimpleResults<>("b", "", "", true),
                        new SimpleResults<>("c", "", "", true)));

        resultRows.forEach(sr::log);
        sr.generateReport();

    }
}