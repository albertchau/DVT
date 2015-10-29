package com.intuit.idea.chopsticks.controllers;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.services.ComparisonService;
import com.intuit.idea.chopsticks.services.ComparisonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.intuit.idea.chopsticks.utils.CollectionUtils.identifyingName;
import static java.util.Objects.nonNull;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/12/15
 * ************************************
 */
public final class DataParityController {

    private Logger logger = LoggerFactory.getLogger(DataParityController.class);

    private DataProvider source;
    private DataProvider target;
    private List<ComparisonService> comparisonServices;

    public DataParityController(DataProvider source, DataProvider target, List<ComparisonService> comparisonServices) {
        this.source = source;
        this.target = target;
        this.comparisonServices = comparisonServices;
    }

    public DataParityController(DataProvider source, DataProvider target) {
        this.source = source;
        this.target = target;
        comparisonServices = new ArrayList<>();
    }

    public void execute() {
        comparisonServices.forEach(comparisonService -> {
            try {
                comparisonService.compare(source, target);
            } catch (Exception e) {
                ComparisonType comparisonType = comparisonService.getType();
                logger.error(comparisonType.stringify() + " for " + identifyingName(source, target) + " failed! Reason: " + e.getMessage() + ".");
                String debugErrorMessage = Arrays.stream(e.getStackTrace())
                        .filter(st -> nonNull(st.getFileName()) && !st.getFileName().equalsIgnoreCase("null"))
                        .filter(st -> st.getClassName().contains("intuit"))
                        .map(st -> st.getFileName() + ":" + st.getLineNumber())
                        .collect(Collectors.joining(" --> "));
                logger.debug(debugErrorMessage);
            }
        });
    }

    public void run() {
        execute();
    }

}
