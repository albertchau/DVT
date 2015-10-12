package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.services.transforms.Extracted;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.containers.CombinedMetadata;
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
public class DefaultExtracter implements Extracter {
    private static Logger logger = LoggerFactory.getLogger(DefaultExtracter.class);

    @Override
    public Extracted extract(Loaded loaded) throws ComparisonException {
        ResultSet srcRowSet = loaded.srcResultSet;
        List<Metadata> srcMetadata = loaded.srcMetadata;
        ResultSet tarRowSet = loaded.tarResultSet;
        List<Metadata> tarMetadata = loaded.tarMetadata;
        List<Comparable[]> sRowList;
        List<Comparable[]> tRowList;
        CombinedMetadata[] metadatas = mergeMetadata(srcMetadata.toArray(new Metadata[srcMetadata.size()]), tarMetadata.toArray(new Metadata[tarMetadata.size()]));
        try {
            sRowList = resultSetToSortedList(srcRowSet, metadatas, CombinedMetadata::getSrc);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving source's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
        }
        try {
            tRowList = resultSetToSortedList(tarRowSet, metadatas, CombinedMetadata::getTar);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving target's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
        }
        return new Extracted(sRowList, tRowList, metadatas);
    }
}
