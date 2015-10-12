package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.services.ComparisonServices;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.intuit.idea.chopsticks.utils.CollectionUtils.isNullOrEmpty;
import static java.util.Objects.isNull;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DefaultLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(DefaultLoader.class);

    @Override
    public Loaded load(DataProvider source, DataProvider target, ComparisonServices type) throws ComparisonException {
        if (isNull(type)) {
            logger.info("Assuming metadata comparison because type was null. For safety, next time - use MetadataLoader.");
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
        try (ResultSet srcData = source.getData(type);
             ResultSet tarData = target.getData(type)) {
            List<Metadata> srcMetadata = source.getMetadata();
            List<Metadata> tarMetadata = target.getMetadata();
            return new Loaded(srcData, srcMetadata, tarData, tarMetadata);
        } catch (SQLException | DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("");
        }
    }
}
