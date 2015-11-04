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
public class FieldType {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String short_name;
    @Basic
    private String long_name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldType)) return false;
        FieldType fieldType = (FieldType) o;
        return Objects.equals(id, fieldType.id) &&
                Objects.equals(short_name, fieldType.short_name) &&
                Objects.equals(long_name, fieldType.long_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, short_name, long_name);
    }
}
