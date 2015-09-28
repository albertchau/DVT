package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.results.ResultSets;
import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import com.intuit.idea.chopsticks.utils.exceptions.DataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createMates;
import static java.util.Collections.singletonList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class CountComparisonService implements ComparisonService {
    private static Logger logger = LoggerFactory.getLogger(CountComparisonService.class);
    private final Set<ResultStore> resultStores;
    private final double thresholdPercent;
    private final DecimalFormat df = new DecimalFormat("#.###");

    public CountComparisonService(Set<ResultStore> resultStores, Double thresholdPercent) throws ComparisonException {
        this.thresholdPercent = thresholdPercent == null ? 0.0 : thresholdPercent;
        if (this.thresholdPercent < 0) {
            logger.error("Threshold percent cannot be less than 0.");
            throw new ComparisonException("Threshold percent cannot be less than 0.");
        }
        this.resultStores = resultStores == null ? new HashSet<>() : resultStores;
    }


    @Override
    public void init() {

    }

    @Override
    public void compare(DataProvider source, DataProvider target) throws ComparisonException {
        try {
            source.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not open connections to source.");
        }
        try {
            target.openConnections();
        } catch (DataProviderException e) {
            e.printStackTrace();
            throw new ComparisonException("Could not open connections to target.");
        }
        try (ResultSets sData = source.getData(this);
             ResultSets tData = target.getData(this)) {
            countCompare(sData, tData);
        } catch (DataProviderException | SQLException e) {
            e.printStackTrace();
            logger.error("Could not get data for comparison.");
            throw new ComparisonException("Could not get data for comparison.");
        }
        source.closeConnections();
        target.closeConnections();
    }

    /**
     * Exposed for testing purposes mainly
     *
     * @param sData ResultSet of count data from source. One column per row and must be some type of Number (Integer)
     * @param tData ResultSet of count data from target. One column per row and must be some type of Number (Integer)
     * @throws ComparisonException if something were to go wrong in setup or execution phase
     */
    public void countCompare(ResultSet sData, ResultSet tData) throws ComparisonException {
        long start = System.nanoTime();
        List<Integer> sRowList;
        try {
            sRowList = resultSetToList(sData);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving source's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving source's resultsets into memory failed.");
        }
        List<Integer> tRowList;
        try {
            tRowList = resultSetToList(tData);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("During setup, retrieving target's resultsets into memory failed.");
            throw new ComparisonException("During setup, retrieving target's resultsets into memory failed.");
        }
        sumAndCompare(sRowList, tRowList);
        long end = System.nanoTime();
        logger.info("start - end /100000 = " + ((end - start) / 1000000));
    }

    private void sumAndCompare(List<Integer> sRowList, List<Integer> tRowList) throws ComparisonException {
        double srcSum = sRowList.stream()
                .mapToDouble(Integer::valueOf)
                .sum();
        double tarSum = tRowList.stream()
                .mapToDouble(Integer::valueOf)
                .sum();
        double percentError;
        if (srcSum == 0) {
            if (tarSum == 0) {
                percentError = 0.0;
            } else {
                throw new ComparisonException("The sum of the source counts was 0.");
            }
        } else {
            percentError = (tarSum - srcSum) / srcSum;
        }
        if (percentError > 0) {
            logger.info("Failed Count Comparison: Target count: "
                    + (df.format(tarSum)) + " was greater than source count: "
                    + (df.format(srcSum)) + " which should not happen.");
        } else if (percentError > thresholdPercent) {
            logger.info("Failed Count Comparison: Target count: "
                    + (df.format(tarSum)) + " was less than source count: "
                    + (df.format(srcSum)) + " producing a percent error: "
                    + (df.format(percentError))
                    + " that was greater than defined threshold for percent error: "
                    + (df.format(thresholdPercent)) + ".");
        } else {
            logger.info("Passed Count Comparison: Target count: "
                    + (df.format(tarSum)) + " source count: "
                    + (df.format(srcSum)) + " producing a percent error: "
                    + (df.format(percentError))
                    + " that was within the defined threshold for percent error: "
                    + (df.format(thresholdPercent)) + ".");
        }
        resultStores.stream()
                .forEach(r -> r.storeRowResults(
                        this,
                        singletonList(createMates(
                                        percentError <= thresholdPercent,
                                        "Count",
                                        "Count",
                                        srcSum,
                                        tarSum,
                                        false)
                        )
                ));
    }

    @Override
    public void finish() {

    }

    private List<Integer> resultSetToList(ResultSet resultSet) throws ComparisonException, SQLException {
        List<Integer> listOfRows = new ArrayList<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        if (columnCount != 1) {
            logger.error("Incorrect resultSet format. Only one column representing the count can be present. Found " + columnCount + " columns instead of one.");
            throw new ComparisonException("Incorrect resultSet format. Only one column representing the count can be present. Found " + columnCount + " columns instead of one.");
        }
        while (resultSet.next()) {
            String strToBeParsed = resultSet.getString(1);
            try {
                listOfRows.add(Integer.valueOf(strToBeParsed));
            } catch (NumberFormatException e) {
                logger.error("NumberFormatException occured when trying to parse: " + (strToBeParsed)
                        + " into an Integer. Please check to make sure query is returning back some number.");
                throw new ComparisonException("NumberFormatException occured when trying to parse: " + (strToBeParsed)
                        + " into an Integer. Please check to make sure query is returning back some number.");
            }
        }
        return listOfRows;
    }

}
