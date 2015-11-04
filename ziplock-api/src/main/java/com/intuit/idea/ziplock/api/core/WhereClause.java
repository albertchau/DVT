package com.intuit.idea.ziplock.api.core;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Copyright 2015
 *
 * @author albert
 */
@Entity
public class WhereClause {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String upper_bound;
    @Basic
    private String lower_bound;
    @Basic
    private String field_name;
    @Basic
    private String equality;
    @Basic
    private String custom_where_clause;
    @ManyToOne(cascade = CascadeType.ALL)
    private FieldType fieldType;
    @ElementCollection
    private Collection<String> inBounds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUpper_bound() {
        return upper_bound;
    }

    public void setUpper_bound(String upper_bound) {
        this.upper_bound = upper_bound;
    }

    public String getLower_bound() {
        return lower_bound;
    }

    public void setLower_bound(String lower_bound) {
        this.lower_bound = lower_bound;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String filed_name) {
        this.field_name = filed_name;
    }

    public String getEquality() {
        return equality;
    }

    public void setEquality(String equality) {
        this.equality = equality;
    }

    public String getCustom_where_clause() {
        return custom_where_clause;
    }

    public void setCustom_where_clause(String custom_where_clause) {
        this.custom_where_clause = custom_where_clause;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public Collection<String> getInBounds() {
        return inBounds;
    }

    public void setInBounds(Collection<String> inBounds) {
        this.inBounds = inBounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhereClause)) return false;
        WhereClause that = (WhereClause) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(upper_bound, that.upper_bound) &&
                Objects.equals(lower_bound, that.lower_bound) &&
                Objects.equals(field_name, that.field_name) &&
                Objects.equals(equality, that.equality) &&
                Objects.equals(custom_where_clause, that.custom_where_clause) &&
                Objects.equals(fieldType, that.fieldType) &&
                Objects.equals(inBounds, that.inBounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, upper_bound, lower_bound, field_name, equality, custom_where_clause, fieldType, inBounds);
    }
}
