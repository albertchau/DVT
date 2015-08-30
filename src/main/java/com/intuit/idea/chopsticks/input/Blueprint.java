package com.intuit.idea.chopsticks.input;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class Blueprint {

    private DataStore source;
    private DataStore target;
    private BlueprintOptions options;
    private BlueprintMappings mappings;

    public Blueprint(DataStore source, DataStore target, BlueprintOptions options, BlueprintMappings mappings) {
        this.source = source;
        this.target = target;
        this.options = options;
        this.mappings = mappings;
    }

    public DataStore getSource() {
        return source;
    }

    public void setSource(DataStore source) {
        this.source = source;
    }

    public DataStore getTarget() {
        return target;
    }

    public void setTarget(DataStore target) {
        this.target = target;
    }

    public BlueprintOptions getOptions() {
        return options;
    }

    public void setOptions(BlueprintOptions options) {
        this.options = options;
    }

    public BlueprintMappings getMappings() {
        return mappings;
    }

    public void setMappings(BlueprintMappings mappings) {
        this.mappings = mappings;
    }
}
