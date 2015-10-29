package com.intuit.idea.ziplock.controllers;

import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.intuit.idea.ziplock.providers.DataProvider;
import com.intuit.idea.ziplock.services.ComparisonService;
import com.intuit.idea.ziplock.services.ComparisonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.intuit.idea.ziplock.utils.Commons.identifierName;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/12/15
 * ************************************
 */
public final class DataParityController {
    private final DataProvider source;
    private final DataProvider target;
    private final List<ComparisonService> comparisonServices;
    private Logger logger = LoggerFactory.getLogger(DataParityController.class);

    public DataParityController(DataProvider source, DataProvider target, List<ComparisonService> comparisonServices) {
        this.source = source;
        this.target = target;
        this.comparisonServices = comparisonServices;
    }

    public DataParityController(DataProvider source, DataProvider target) {
        this.source = source;
        this.target = target;
        this.comparisonServices = new ArrayList<>();
    }

    private void execute() {
        comparisonServices.stream()
                .forEach(this::runComparisonService);
    }

    private void runComparisonService(ComparisonService comparisonService) {
        try {
            comparisonService.compare(source, target);
        } catch (Exception e) {
            ComparisonType comparisonType = comparisonService.getType();
            logger.error(comparisonType.stringify() + " for " + identifierName(source, target) + " failed due to error! Reason: " + e.getMessage() + ".");
            String debugErrorMessage = Stream.of(e.getStackTrace())
                    .filter(st -> Objects.nonNull(st.getFileName()) && !st.getFileName().equals("null"))
                    .filter(st -> st.getClassName().contains("intuit"))
                    .map(st -> st.getFileName() + ":" + st.getLineNumber())
                    .collect(Collectors.joining(" --> "));
            logger.debug(debugErrorMessage);
        }
    }

    public void run() {
        setup();
        execute();
    }

    private void setup() {
        /* Programmatically set the logging level for console */
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        Appender<ILoggingEvent> console = root.getAppender("CONSOLE");
        console.clearAllFilters();
        ThresholdFilter thresholdFilter = new ThresholdFilter();
        thresholdFilter.setLevel("INFO");
        thresholdFilter.start();
        console.addFilter(thresholdFilter);
    }

}
