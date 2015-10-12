package com.intuit.idea.chopsticks.services.transforms;

import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;

import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class Extracted {
    public final List<Comparable[]> srcList;
    public final List<Comparable[]> tarList;
    public final CombinedMetadata[] metadatas;

    public Extracted(List<Comparable[]> srcList, List<Comparable[]> tarList, CombinedMetadata[] metadatas) {
        this.srcList = srcList;
        this.tarList = tarList;
        this.metadatas = metadatas;
    }

}
