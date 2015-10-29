package com.intuit.idea.ziplock.services.strategies.comparers;

import com.intuit.idea.ziplock.results.ResultStore;
import com.intuit.idea.ziplock.utils.containers.Extracted;
import com.intuit.idea.ziplock.utils.exceptions.ComparisonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;

import static com.intuit.idea.ziplock.results.ColumnComparisonResult.createFailure;
import static com.intuit.idea.ziplock.results.ColumnComparisonResult.createMates;
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
    private boolean isPassed = true;

    public CountComparer(Double thresholdPercent) throws ComparisonException {
        this.thresholdPercent = thresholdPercent == null ? 0.0 : thresholdPercent;
        if (this.thresholdPercent < 0) {
            throw new ComparisonException("Threshold percent cannot be less than 0.");
        }
    }

    @Override
    public void compare(Extracted extracted, Set<ResultStore> resultStores) throws ComparisonException {
        logger.debug("Comparing count data sets...");
        List<Comparable[]> sRowList = extracted.srcList;
        List<Comparable[]> tRowList = extracted.tarList;
        double srcSum;
        double tarSum;
        try {
            srcSum = getSum(sRowList);
        } catch (ClassCastException e) {
            resultStores.stream()
                    .forEach(r -> r.storeRowResults(singletonList(createFailure())));
            throw new ComparisonException("Could not cast source rows to integers.");
        }
        try {
            tarSum = getSum(tRowList);
        } catch (Exception e) {
            resultStores.stream()
                    .forEach(r -> r.storeRowResults(singletonList(createFailure())));
            throw new ComparisonException("Could not cast target rows to integers.");
        }
        double percentError;
        if (srcSum == 0) {
            if (tarSum == 0) {
                percentError = 0.0;
            } else {
                percentError = 1.0;
                isPassed = false;
                logger.warn("The sum of the source counts was 0.");
            }
        } else {
            percentError = (tarSum - srcSum) / srcSum;
        }
        if (percentError > 0) {
            isPassed = false;
            logger.warn("Failed Count Comparison: Target count: "
                    + (df.format(tarSum)) + " was greater than source count: "
                    + (df.format(srcSum)) + " which should not happen.");
        } else if (percentError > thresholdPercent) {
            isPassed = false;
            logger.warn("Failed Count Comparison: Target count: "
                    + (df.format(tarSum)) + " was less than source count: "
                    + (df.format(srcSum)) + " producing a percent error: "
                    + (df.format(percentError))
                    + " that was greater than defined threshold for percent error: "
                    + (df.format(thresholdPercent)) + ".");
        } else {
            logger.debug("Passed Count Comparison: Target count: "
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
        logger.debug("Successfully compared count data resulting in: " + (isPassed ? "[PASSED]" : "[FAILED]"));
    }

    @Override
    public Boolean getResult() {
        return isPassed;
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
