package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.services.transforms.Extracted;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class MetadataExtracter implements Extracter {
    private static Logger logger = LoggerFactory.getLogger(MetadataExtracter.class);

    @Override
    public Extracted extract(Loaded loaded) throws ComparisonException {
        List<Metadata> srcMetadata = loaded.srcMetadata;
        List<Metadata> tarMetadata = loaded.tarMetadata;
        List<Comparable[]> srcMetadataAsComparables = unaryToListOfComparables(srcMetadata);
        List<Comparable[]> tarMetadataAsComparables = unaryToListOfComparables(tarMetadata);
        return new Extracted(srcMetadataAsComparables, tarMetadataAsComparables, new CombinedMetadata[]{});
    }

    private List<Comparable[]> unaryToListOfComparables(List<? extends Comparable> metadata) {
        return metadata.stream()
                .map(md -> new Comparable[]{md})
                .collect(toList());
    }
}

