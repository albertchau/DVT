package com.intuit.idea.ziplock.api.core;

import javax.persistence.*;
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
                name = "Reporter.findAll",
                query = "SELECT r FROM Reporter r"
        )
})
public class Reporter {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String verbosity_level;
    @Basic
    private String host;
    @Basic
    private String name;
    @Basic
    private Long port;
    @Basic
    private String cluster_name;
    @Basic
    private String protocol;
    @Basic
    private String idx;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerbosity_level() {
        return verbosity_level;
    }

    public void setVerbosity_level(String verbosity_level) {
        this.verbosity_level = verbosity_level;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getCluster_name() {
        return cluster_name;
    }

    public void setCluster_name(String cluster_name) {
        this.cluster_name = cluster_name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String index) {
        this.idx = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reporter)) return false;
        Reporter reporter = (Reporter) o;
        return Objects.equals(id, reporter.id) &&
                Objects.equals(verbosity_level, reporter.verbosity_level) &&
                Objects.equals(host, reporter.host) &&
                Objects.equals(name, reporter.name) &&
                Objects.equals(port, reporter.port) &&
                Objects.equals(cluster_name, reporter.cluster_name) &&
                Objects.equals(protocol, reporter.protocol) &&
                Objects.equals(idx, reporter.idx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, verbosity_level, host, name, port, cluster_name, protocol, idx);
    }
}
