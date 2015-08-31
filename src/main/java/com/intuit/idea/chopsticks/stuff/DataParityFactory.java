package com.intuit.idea.chopsticks.stuff;

import com.google.gson.JsonObject;
import com.typesafe.config.Config;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public final class DataParityFactory {
    private DataParityFactory() {
    }

    public static List<DataParity> create(Config config) {
        return null;
    }

    public static List<DataParity> create(JsonObject config) {
        return null;
    }

    public static List<DataParity> create(String config) {
        return null;
    }

    public static DataParity create(DataProvider source, DataProvider target, ComparisonOptions options, ComparisonMappings mappings) {
        return null;
    }

    public static DataParity create(DataProvider source, DataProvider target, List<ComparisonEngine> comparisonEngines) {
        return null;
    }

    public static DataParity create(DataProvider source, DataProvider target, ComparisonEngine comparisonEngines) {
        return null;
    }

//    DataProvider createSource();
//
//    DataProvider createTarget();
//
//    ComparisonOptions createOptions();
//
//    ComparisonMappings createMappings();
//
//    List<ComparisonEngine> createComparisonEngines();
}
