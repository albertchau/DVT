package com.intuit.idea.ziplock.api.core;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Config.findAll",
                query = "SELECT r FROM Config r"
        )
})
public class Config {

    private Long id;
    private String label;
    private Dataset dataset;
    private Collection<RelationMapConfig> relationMapConfigs;
    private String email;
    private Boolean debug;
    private Collection<Reporter> reporters;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    public String getLabel() {
        return label;
    }

    public void setLabel(String name) {
        this.label = name;
    }

    @ManyToOne
    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public Collection<RelationMapConfig> getRelationMapConfigs() {
        return relationMapConfigs;
    }

    public void setRelationMapConfigs(Collection<RelationMapConfig> relationMapConfigs) {
        this.relationMapConfigs = relationMapConfigs;
    }

    @Basic
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    public Collection<Reporter> getReporters() {
        return reporters;
    }

    public void setReporters(Collection<Reporter> reporters) {
        this.reporters = reporters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Config)) return false;
        Config config = (Config) o;
        return Objects.equals(id, config.id) &&
                Objects.equals(label, config.label) &&
                Objects.equals(dataset, config.dataset) &&
                Objects.equals(relationMapConfigs, config.relationMapConfigs) &&
                Objects.equals(email, config.email) &&
                Objects.equals(debug, config.debug) &&
                Objects.equals(reporters, config.reporters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, dataset, relationMapConfigs, email, debug, reporters);
    }
}
