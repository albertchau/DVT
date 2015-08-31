package com.intuit.idea.chopsticks.stuff;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public interface DataProvider {

    List getData();

    List getExistenceData();

    List getPrimaryKeys();

    Integer getCountData();

    DataProviderType getType();

    String getDataQuery();

    String getExistenceQuery();

    String getCountQuery();

    List getMetadata();

    String getName();

}
