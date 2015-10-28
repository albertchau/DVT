package com.intuit.idea.chopsticks.services.strategies.extractors;

import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;
import com.intuit.idea.chopsticks.utils.containers.Extracted;
import com.intuit.idea.chopsticks.utils.containers.Loaded;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.intuit.idea.chopsticks.utils.ComparisonUtils.mergeMetadata;
import static com.intuit.idea.chopsticks.utils.ComparisonUtils.resultSetToSortedList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DefaultExtractor implements Extractor {
    private static Logger logger = LoggerFactory.getLogger(DefaultExtractor.class);

    @Override
    public Extracted extract(Loaded loaded) throws ComparisonException {
        ResultSet srcRowSet = loaded.srcResultSet;
        List<Metadata> srcMetadata = loaded.srcMetadata;
        ResultSet tarRowSet = loaded.tarResultSet;
        List<Metadata> tarMetadata = loaded.tarMetadata;
        List<Comparable[]> sRowList;
        List<Comparable[]> tRowList;
        CombinedMetadata[] metadatas = mergeMetadata(srcMetadata, tarMetadata);
        try {
            sRowList = resultSetToSortedList(srcRowSet, metadatas, CombinedMetadata::getSrc);
        } catch (SQLException e) {
            logger.error("During setup, retrieving source's resultsets into memory failed: " + e.getMessage());
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed: " + e.getMessage());
        }
        try {
            tRowList = resultSetToSortedList(tarRowSet, metadatas, CombinedMetadata::getTar);
        } catch (SQLException e) {
            logger.error("During setup, retrieving target's resultsets into memory failed: " + e.getMessage());
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed: " + e.getMessage());
        }
        return new Extracted(sRowList, tRowList, metadatas);
    }
}
