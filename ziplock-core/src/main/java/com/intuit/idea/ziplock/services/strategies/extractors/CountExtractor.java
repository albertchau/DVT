package com.intuit.idea.ziplock.services.strategies.extractors;

import com.intuit.idea.ziplock.utils.containers.Extracted;
import com.intuit.idea.ziplock.utils.containers.Loaded;
import com.intuit.idea.ziplock.utils.containers.Metadata;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class CountExtractor implements Extractor {
    private static Logger logger = LoggerFactory.getLogger(CountExtractor.class);

    @Override
    public Extracted extract(Loaded loaded) throws ComparisonException {
        logger.debug("Extracting count data.");
        ResultSet srcRowSet = loaded.srcResultSet;
        List<Metadata> srcMetadata = loaded.srcMetadata;
        ResultSet tarRowSet = loaded.tarResultSet;
        List<Metadata> tarMetadata = loaded.tarMetadata;
        List<Comparable[]> sRowList;
        List<Comparable[]> tRowList;
        try {
            sRowList = resultSetToList(srcRowSet, srcMetadata);
        } catch (SQLException e) {
            throw new ComparisonException("Failure during EXTRACT when trying to retrieve source's result sets: " + e.getMessage(), e);
        }
        try {
            tRowList = resultSetToList(tarRowSet, tarMetadata);
        } catch (SQLException e) {
            throw new ComparisonException("Failure during EXTRACT when trying to retrieve target's result sets: " + e.getMessage(), e);
        }
        logger.debug("Successfully extracted count data.");
        return new Extracted(sRowList, tRowList, null);
    }

    private List<Comparable[]> resultSetToList(ResultSet resultSet, List<Metadata> metadata) throws ComparisonException, SQLException {
        List<Comparable[]> listOfRows = new ArrayList<>();
        int columnCount = metadata.size();
        if (columnCount != 1) {
            throw new ComparisonException("Incorrect resultSet format. Only one column representing the count can be present. Found " + columnCount + " columns instead of one.");
        }
        while (resultSet.next()) {
            String strToBeParsed = resultSet.getString(1);
            listOfRows.add(new Comparable[]{strToBeParsed});
        }
        return listOfRows;
    }

}
