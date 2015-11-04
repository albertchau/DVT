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
public class TestType {
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
        if (!(o instanceof TestType)) return false;
        TestType testType = (TestType) o;
        return Objects.equals(id, testType.id) &&
                Objects.equals(label, testType.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }
}
