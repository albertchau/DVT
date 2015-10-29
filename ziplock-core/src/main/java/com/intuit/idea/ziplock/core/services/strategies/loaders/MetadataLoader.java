package com.intuit.idea.ziplock.core.services.strategies.loaders;

import com.intuit.idea.ziplock.core.providers.DataProvider;
import com.intuit.idea.ziplock.core.services.ComparisonType;
import com.intuit.idea.ziplock.core.utils.containers.Loaded;
import com.intuit.idea.ziplock.core.utils.containers.Metadata;
import com.intuit.idea.ziplock.core.utils.exceptions.ComparisonException;
import com.intuit.idea.ziplock.core.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.intuit.idea.ziplock.core.utils.CollectionUtils.isNullOrEmpty;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class MetadataLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(MetadataLoader.class);

    @Override
    public Loaded load(DataProvider source, DataProvider target, ComparisonType type) throws ComparisonException {
        logger.debug("Loading metadata.");
        List<Metadata> srcMetadata;
        List<Metadata> tarMetadata;
        try {
            srcMetadata = getMetadatasFrom(source);
        } catch (DataProviderException | ComparisonException e) {
            throw new ComparisonException("Failed to LOAD source metadata for metadata comparison because: " + e.getMessage(), e);
        }
        try {
            tarMetadata = getMetadatasFrom(target);
        } catch (DataProviderException | ComparisonException e) {
            throw new ComparisonException("Failed to LOAD target metadata for metadata comparison because: " + e.getMessage(), e);
        }
        logger.debug("Successfully loaded metadata.");
        return new Loaded(null, srcMetadata, null, tarMetadata);
    }

    private List<Metadata> getMetadatasFrom(DataProvider dataProvider) throws ComparisonException {
        List<Metadata> metadata;
        metadata = dataProvider.getMetadata();
        if (isNullOrEmpty(metadata)) {
            throw new ComparisonException("Obtained metadata, but it was null or empty.");
        }
        return metadata;
    }
}
