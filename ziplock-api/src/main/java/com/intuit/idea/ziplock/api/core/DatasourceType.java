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
public class DatasourceType {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String Label;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatasourceType)) return false;
        DatasourceType that = (DatasourceType) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(Label, that.Label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Label);
    }
}
