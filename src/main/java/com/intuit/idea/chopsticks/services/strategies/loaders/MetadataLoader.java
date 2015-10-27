package com.intuit.idea.chopsticks.services.strategies.loaders;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.services.ComparisonType;
import com.intuit.idea.chopsticks.utils.containers.Loaded;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.intuit.idea.chopsticks.utils.CollectionUtils.isNullOrEmpty;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class MetadataLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(MetadataLoader.class);

    @Override
    public Loaded load(DataProvider source, DataProvider target, ComparisonType type) throws ComparisonException {
        try {
            List<Metadata> srcMetadata = source.getMetadata();
            List<Metadata> tarMetadata = target.getMetadata();
            if (isNullOrEmpty(srcMetadata)) {
                logger.error("Could not find source metadata.;");
                throw new ComparisonException("Could not find source metadata.");
            }
            if (isNullOrEmpty(tarMetadata)) {
                logger.error("Could not find source metadata.;");
                throw new ComparisonException("Could not find source metadata.");
            }
            return new Loaded(null, srcMetadata, null, tarMetadata);
        } catch (DataProviderException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
    }
}
