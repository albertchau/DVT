package com.intuit.idea.chopsticks.input;

import com.typesafe.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    public Parser(String arg) {
    }

    public static List<Blueprint> parse(String fileName) {
        File configFile = new File(fileName);
        List<Blueprint> blueprints = new ArrayList<Blueprint>();

        if (!configFile.exists())
            throw new NullPointerException("File DNE");

        Config rootConfig = ConfigFactory.parseFileAnySyntax(configFile);

        for (Config blueprintConfig : rootConfig.getConfigList("blueprints")) {
            Blueprint bp = blueprintFromConfig(blueprintConfig);
            blueprints.add(bp);
        }
        return null;
    }

    public static Blueprint blueprintFromConfig(Config config) {
        DataStore source = dataStoreFromConfig(config, "source");
        DataStore target = dataStoreFromConfig(config, "target");
        BlueprintOptions options = optionsFromConfig(config);
        BlueprintMappings mappings = mappingsFromConfig(config);
        Blueprint rtn = new Blueprint(source, target, options, mappings);

        return rtn;
    }

    public static BlueprintMappings mappingsFromConfig(Config config) {
        return null;
    }

    public static BlueprintOptions optionsFromConfig(Config config) {
        return null;
    }

    public static DataStore dataStoreFromConfig(Config config, String selector) {
        if (!config.hasPath(selector)) {
            logger.error("The Configuration needs a " + selector + " in order to proceed");
            throw new ConfigException.Missing(selector);
        }
        Config dataStoreConfig = config.getConfig(selector);

        DataStoreType type;
        try {
            type = DataStoreType.valueOf(dataStoreConfig.getString("type").toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid type for your " + selector + " data store. Please choose from: " + Arrays.toString(DataStoreType.values()));
            throw e;
        } catch (ConfigException.Missing e) {
            logger.error("Your " + selector + " needs a type. Currently empty");
            throw e;
        }

        DataStoreProvider provider;
        switch (type) {
            case TABLE:
                break;
            case QUERY:
                break;
            case FLATFILE:
                break;
            case AWS:
                break;
        }

    }
}
