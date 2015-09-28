package com.intuit.idea.chopsticks.results;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class InMemoryResultStoreTest {

    private Random rnd = new Random();

    @Test
    public void testReport() throws Exception {
        List<String> colHeaders = Arrays.asList("row1", "row2", "row3", "row4", "row5");
        InMemoryResultStore inMemoryResultStore = new InMemoryResultStore();
        IntStream.range(0, 10000)
                .boxed()
                .map(i -> colHeaders.stream()
                        .map(header -> ColumnComparisonResult.createMates(roolean(), header, header, header, header, false))
                        .collect(Collectors.toList()))
                .forEach((columnResults) -> inMemoryResultStore.storeRowResults(null, columnResults));
        inMemoryResultStore.report();
    }

    public boolean roolean() {
        return rnd.nextBoolean();
    }
}