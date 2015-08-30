package com.intuit.idea.chopsticks;

import com.intuit.idea.chopsticks.input.Parser;
import com.typesafe.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class Main {

    static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        for (String arg : args) {
            Config tmp = ConfigFactory.parseFileAnySyntax(new File(arg));
            Config resolve = tmp.resolve();
            System.out.println(resolve.getConfigList("blueprints").get(0).getString("source.type"));
            logger.info("Hello World");
        }
    }
}
