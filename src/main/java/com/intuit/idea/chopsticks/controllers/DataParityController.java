package com.intuit.idea.chopsticks.controllers;

import com.intuit.idea.chopsticks.providers.DataProvider;
import com.intuit.idea.chopsticks.services.ComparisonService;
import com.intuit.idea.chopsticks.services.ComparisonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.intuit.idea.chopsticks.utils.CollectionUtils.toProperCase;

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

    public void registerComparisonService(ComparisonService comparisonService) {
        comparisonServices.add(comparisonService);
    }

    public void before() {
        logger.info("before");
    }

    public void execute() {
        logger.info("execute");
        comparisonServices.forEach(comparisonService -> {
            try {
                comparisonService.compare(source, target);
            } catch (Exception e) {
                ComparisonType ToCS = comparisonService.getType();
                logger.error(toProperCase(ToCS.toString()) + " Failed!!! Reason: " + e.getMessage());
            }
        });
    }

    public void after() {
        logger.info("after");
    }

    public void run() {
        logger.info("run");
        before();
        execute();
        after();
    }

}
