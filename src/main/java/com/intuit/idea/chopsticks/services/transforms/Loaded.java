package com.intuit.idea.chopsticks.services.transforms;

import com.intuit.idea.chopsticks.utils.containers.Metadata;

import java.sql.ResultSet;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class Loaded {
    public final ResultSet srcResultSet;
    public final List<Metadata> srcMetadata;
    public final ResultSet tarResultSet;
    public final List<Metadata> tarMetadata;

    public Loaded(ResultSet srcResultSet, List<Metadata> srcMetadata, ResultSet tarResultSet, List<Metadata> tarMetadata) {
        this.srcResultSet = srcResultSet;
        this.srcMetadata = srcMetadata;
        this.tarResultSet = tarResultSet;
        this.tarMetadata = tarMetadata;
    }

}
