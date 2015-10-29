package com.intuit.idea.ziplock.services.strategies.loaders;

import com.intuit.idea.ziplock.providers.DataProvider;
import com.intuit.idea.ziplock.services.ComparisonType;
import com.intuit.idea.ziplock.utils.containers.Loaded;
import com.intuit.idea.ziplock.utils.containers.Metadata;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;
import com.intuit.idea.ziplock.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.intuit.idea.ziplock.utils.CollectionUtils.isNullOrEmpty;
import static com.intuit.idea.ziplock.utils.ComparisonUtils.extractAllMetadata;
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
            logger.debug("Assuming metadata comparison because type was " + type + ". For safety, next time - use MetadataLoader.");
            return new MetadataLoader().load(source, target, type);
        }
        ResultSet srcData;
        ResultSet tarData;
        List<Metadata> srcMetadata;
        List<Metadata> tarMetadata;
        try {
            srcData = source.getData(type);
        } catch (DataProviderException e) {
            throw new ComparisonException("Failed to LOAD source data for comparison because: " + e.getMessage(), e);
        }
        try {
            tarData = target.getData(type);
        } catch (DataProviderException e) {
            throw new ComparisonException("Failed to LOAD target data for comparison because: " + e.getMessage(), e);
        }
        try {
            srcMetadata = getMetadataFrom(source, type, srcData);
        } catch (ComparisonException | SQLException e) {
            throw new ComparisonException("Failed to LOAD source metadata for comparison because: " + e.getMessage(), e);
        }
        try {
            tarMetadata = getMetadataFrom(target, type, tarData);
        } catch (SQLException e) {
            throw new ComparisonException("Failed to LOAD target metadata for comparison because: " + e.getMessage(), e);
        }
        logger.debug("Successfully loaded data.");
        return new Loaded(srcData, srcMetadata, tarData, tarMetadata);
    }

    private List<Metadata> getMetadataFrom(DataProvider dataProvider, ComparisonType type, ResultSet data) throws ComparisonException, SQLException {
        List<Metadata> metadata = null;
        switch (type) {
            case DATA:
                metadata = dataProvider.getMetadata();
                break;
            case EXISTENCE:
            case COUNT:
                metadata = extractAllMetadata(data);
                break;
        }
        if (isNullOrEmpty(metadata)) {
            throw new ComparisonException("Obtained metadata, but it was null or empty.");
        }
        return metadata;
    }

}