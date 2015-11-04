package com.intuit.idea.ziplock.api.core;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Copyright 2015
 *
 * @author albert
 */
@Entity
public class RunStatus {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String label;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RunStatus)) return false;
        RunStatus runStatus = (RunStatus) o;
        return Objects.equals(id, runStatus.id) &&
                Objects.equals(label, runStatus.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
}
