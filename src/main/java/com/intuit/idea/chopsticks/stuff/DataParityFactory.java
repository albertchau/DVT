package com.intuit.idea.chopsticks.stuff;

import com.typesafe.config.Config;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface DataParityFactory {

    DataProvider createSource();

    DataProvider createTarget();

    ComparisonOptions createOptions();

    ComparisonMappings createMappings();

    List<ComparisonEngine> createComparisonEngines();
}
