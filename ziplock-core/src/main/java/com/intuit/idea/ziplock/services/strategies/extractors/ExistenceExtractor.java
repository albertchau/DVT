package com.intuit.idea.ziplock.services.strategies.extractors;

import com.intuit.idea.ziplock.utils.containers.CombinedMetadata;
import com.intuit.idea.ziplock.utils.containers.Extracted;
import com.intuit.idea.ziplock.utils.containers.Loaded;
import com.intuit.idea.ziplock.utils.containers.Metadata;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static com.intuit.idea.ziplock.utils.ComparisonUtils.mergeMetadata;
import static com.intuit.idea.ziplock.utils.ComparisonUtils.resultSetToSortedList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class ExistenceExtractor implements Extractor {
    private static Logger logger = LoggerFactory.getLogger(ExistenceExtractor.class);

    @Override
    public Extracted extract(Loaded loaded) throws ComparisonException {
        logger.debug("Extracting existence data.");
        ResultSet srcRowSet = loaded.srcResultSet;
        List<Metadata> srcMetadata = loaded.srcMetadata;
        ResultSet tarRowSet = loaded.tarResultSet;
        List<Metadata> tarMetadata = loaded.tarMetadata;
        List<Comparable[]> sRowList;
        List<Comparable[]> tRowList;
        CombinedMetadata[] allMetadatas = mergeMetadata(srcMetadata, tarMetadata);
        CombinedMetadata[] pkMetadata = Arrays.stream(allMetadatas)
                .filter(CombinedMetadata::isPk)
                .toArray(CombinedMetadata[]::new);
        try {
            sRowList = resultSetToSortedList(srcRowSet, pkMetadata, CombinedMetadata::getSrc);
        } catch (SQLException e) {
            throw new ComparisonException("Failure during EXTRACT when trying to retrieve source's result sets: " + e.getMessage(), e);
        }
        try {
            tRowList = resultSetToSortedList(tarRowSet, pkMetadata, CombinedMetadata::getTar);
        } catch (SQLException e) {
            throw new ComparisonException("Failure during EXTRACT when trying to retrieve target's result sets: " + e.getMessage(), e);
        }
        logger.debug("Successfully extracted existence data.");
        return new Extracted(sRowList, tRowList, pkMetadata);
    }

}
