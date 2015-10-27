package com.intuit.idea.chopsticks.services.strategies.comparers;

import com.intuit.idea.chopsticks.results.ResultStore;
import com.intuit.idea.chopsticks.utils.containers.Extracted;
import com.intuit.idea.chopsticks.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;

import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createFailure;
import static com.intuit.idea.chopsticks.results.ColumnComparisonResult.createMates;
import static java.util.Collections.singletonList;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class CountComparer implements Comparer {
    private static Logger logger = LoggerFactory.getLogger(CountComparer.class);
    private final double thresholdPercent;
    private final DecimalFormat df = new DecimalFormat("#.###");

    public CountComparer(Double thresholdPercent) throws ComparisonException {
        this.thresholdPercent = thresholdPercent == null ? 0.0 : thresholdPercent;
        if (this.thresholdPercent < 0) {
            logger.error("Threshold percent cannot be less than 0.");
            throw new ComparisonException("Threshold percent cannot be less than 0.");
        }
    }

    @Override
    public void compare(Extracted extracted, Set<ResultStore> resultStores) {
        List<Comparable[]> sRowList = extracted.srcList;
        List<Comparable[]> tRowList = extracted.tarList;
        double srcSum;
        double tarSum;
        try {
            srcSum = getSum(sRowList);
        } catch (ClassCastException e) {
            logger.error("Could not cast source rows to integers.");
            resultStores.stream()
                    .forEach(r -> r.storeRowResults(singletonList(createFailure())));
            return;
        }
        try {
            tarSum = getSum(tRowList);
        } catch (Exception e) {
            logger.error("Could not cast target rows to integers.");
            resultStores.stream()
                    .forEach(r -> r.storeRowResults(singletonList(createFailure())));
            return;
        }
        double percentError;
        if (srcSum == 0) {
            if (tarSum == 0) {
                percentError = 0.0;
            } else {
                percentError = 1.0;
                logger.warn("The sum of the source counts was 0.");
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
        final double finalPercentError = percentError;
        resultStores.stream()
                .forEach(r -> r.storeRowResults(
                        singletonList(createMates(
                                finalPercentError <= thresholdPercent,
                                "Count",
                                "Count",
                                srcSum,
                                tarSum,
                                false)
                        )
                ));
    }

    private double getSum(List<Comparable[]> rowList) {
        ToDoubleFunction<Comparable[]> ComparableArrayToDoubles = (Comparable[] carr) ->
                Arrays.stream(carr)
                        .mapToDouble(c -> Integer.valueOf(c.toString()))
                        .sum();
        return rowList.stream()
                .mapToDouble(ComparableArrayToDoubles)
                .sum();
    }

}
