package com.intuit.idea.chopsticks.services.strategies.loaders;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.services.ComparisonType;
import com.intuit.idea.chopsticks.utils.containers.Loaded;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.intuit.idea.chopsticks.utils.ComparisonUtils.extractAllMetadata;
import static java.util.Objects.isNull;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DefaultLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(DefaultLoader.class);

    @Override
    public Loaded load(DataProvider source, DataProvider target, ComparisonType type) throws ComparisonException {
        if (type == ComparisonType.METADATA || isNull(type)) {
            logger.info("Assuming metadata comparison because type was " + type + ". For safety, next time - use MetadataLoader.");
            return new MetadataLoader().load(source, target, type);
        }
        try {
            ResultSet srcData = source.getData(type);
            ResultSet tarData = target.getData(type);
            List<Metadata> srcMetadata = null;
            List<Metadata> tarMetadata = null;
            switch (type) {
                case DATA:
                    srcMetadata = source.getMetadata();
                    tarMetadata = target.getMetadata();
                    break;
                case EXISTENCE:
                case COUNT:
                    srcMetadata = extractAllMetadata(srcData);
                    tarMetadata = extractAllMetadata(tarData);
                    break;
            }
            return new Loaded(srcData, srcMetadata, tarData, tarMetadata);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not get data from sources/targets.");
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not extract metadata from sources/targets.");
        }
    }
}
