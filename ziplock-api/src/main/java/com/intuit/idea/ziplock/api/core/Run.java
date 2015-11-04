package com.intuit.idea.ziplock.api.core;

import javax.persistence.*;
import java.sql.Timestamp;
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
                name = "Run.findAll",
                query = "SELECT distinct r " +
                        "FROM Run as r "
//                        +
//                        "left join r.config as rc " +
//                        "left join fetch rc.relationMapConfigs"
        )
})
public class Run {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private Timestamp created_date;
    @Basic
    private Timestamp start_timestamp;
    @Basic
    private Timestamp end_timestamp;
    @ManyToOne
    private RunStatus runStatus;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Config config;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public Timestamp getStart_timestamp() {
        return start_timestamp;
    }

    public void setStart_timestamp(Timestamp start_timestamp) {
        this.start_timestamp = start_timestamp;
    }

    public Timestamp getEnd_timestamp() {
        return end_timestamp;
    }

    public void setEnd_timestamp(Timestamp end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public RunStatus getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(RunStatus runStatusId) {
        this.runStatus = runStatusId;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Run)) return false;
        Run run = (Run) o;
        return Objects.equals(id, run.id) &&
                Objects.equals(created_date, run.created_date) &&
                Objects.equals(start_timestamp, run.start_timestamp) &&
                Objects.equals(end_timestamp, run.end_timestamp) &&
                Objects.equals(runStatus, run.runStatus) &&
                Objects.equals(config, run.config);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created_date, start_timestamp, end_timestamp, runStatus, config);
    }
}
