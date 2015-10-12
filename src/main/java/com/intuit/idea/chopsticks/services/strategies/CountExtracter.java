package com.intuit.idea.chopsticks.services.strategies;

import com.intuit.idea.chopsticks.services.transforms.Extracted;
import com.intuit.idea.chopsticks.services.transforms.Loaded;
import com.intuit.idea.chopsticks.utils.containers.Metadata;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
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
public class CountExtracter implements Extracter {
    private static Logger logger = LoggerFactory.getLogger(CountExtracter.class);

    @Override
    public Extracted extract(Loaded loaded) throws ComparisonException {
        ResultSet srcRowSet = loaded.srcResultSet;
        List<Metadata> srcMetadata = loaded.srcMetadata;
        ResultSet tarRowSet = loaded.tarResultSet;
        List<Metadata> tarMetadata = loaded.tarMetadata;
        List<Comparable[]> sRowList;
        List<Comparable[]> tRowList;
        try {
            sRowList = resultSetToList(srcRowSet, srcMetadata);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving source's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
        }
        try {
            tRowList = resultSetToList(tarRowSet, tarMetadata);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving target's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
        }
        return new Extracted(sRowList, tRowList, null);
    }

    private List<Comparable[]> resultSetToList(ResultSet resultSet, List<Metadata> metadata) throws ComparisonException, SQLException {
        List<Comparable[]> listOfRows = new ArrayList<>();
        int columnCount = metadata.size();
        if (columnCount != 1) {
            logger.error("Incorrect resultSet format. Only one column representing the count can be present. Found " + columnCount + " columns instead of one.");
            throw new ComparisonException("Incorrect resultSet format. Only one column representing the count can be present. Found " + columnCount + " columns instead of one.");
        }
        while (resultSet.next()) {
            String strToBeParsed = resultSet.getString(1);
            listOfRows.add(new Comparable[]{strToBeParsed});
        }
        return listOfRows;
    }

}
