package com.intuit.idea.ziplock.utils.loggers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

import java.util.Arrays;
import java.util.List;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/28/15
 * ************************************
 */
public class ConsoleFilter extends AbstractMatcherFilter {

    @Override
    public FilterReply decide(Object event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        LoggingEvent loggingEvent = (LoggingEvent) event;

        List<Level> eventsToKeep = Arrays.asList(Level.INFO, Level.ERROR);
        if (eventsToKeep.contains(loggingEvent.getLevel())) {
            return FilterReply.ACCEPT;
        } else {
            return FilterReply.DENY;
        }
    }

}