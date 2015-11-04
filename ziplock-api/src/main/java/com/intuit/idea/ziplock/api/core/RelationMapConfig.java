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
                name = "RelationMapConfig.findAll",
                query = "SELECT r FROM RelationMapConfig r"
        )
})
public class RelationMapConfig {
    private Long id;
    private Collection<Config> configs;
    private Long fetch_amount;
    private DateFormat dateFormatA;
    private DateFormat dateFormatB;
    private OrderDirection orderDirectionA;
    private OrderDirection orderDirectionB;
    private TestType testTypeA;
    private TestType testTypeB;
    private Collection<WhereClause> whereClausesA;
    private Collection<WhereClause> whereClausesB;
    private RelationMap relationMap;
    private Collection<String> primaryKeys;
    private Collection<String> includedColumns;
    private Collection<String> excludedColumns;
    private Collection<Comparison> comparisons;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "relationMapConfigs")
    public Collection<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(Collection<Config> configs) {
        this.configs = configs;
    }

    @Basic
    public Long getFetch_amount() {
        return fetch_amount;
    }

    public void setFetch_amount(Long fetch_amount) {
        this.fetch_amount = fetch_amount;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public DateFormat getDateFormatA() {
        return dateFormatA;
    }

    public void setDateFormatA(DateFormat dateFormatA) {
        this.dateFormatA = dateFormatA;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public DateFormat getDateFormatB() {
        return dateFormatB;
    }

    public void setDateFormatB(DateFormat dateFormatB) {
        this.dateFormatB = dateFormatB;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OrderDirection getOrderDirectionA() {
        return orderDirectionA;
    }

    public void setOrderDirectionA(OrderDirection orderDirectionA) {
        this.orderDirectionA = orderDirectionA;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public OrderDirection getOrderDirectionB() {
        return orderDirectionB;
    }

    public void setOrderDirectionB(OrderDirection orderDirectionB) {
        this.orderDirectionB = orderDirectionB;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public TestType getTestTypeA() {
        return testTypeA;
    }

    public void setTestTypeA(TestType testTypeA) {
        this.testTypeA = testTypeA;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public TestType getTestTypeB() {
        return testTypeB;
    }

    public void setTestTypeB(TestType testTypeB) {
        this.testTypeB = testTypeB;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    public Collection<WhereClause> getWhereClausesA() {
        return whereClausesA;
    }

    public void setWhereClausesA(Collection<WhereClause> whereClausesA) {
        this.whereClausesA = whereClausesA;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    public Collection<WhereClause> getWhereClausesB() {
        return whereClausesB;
    }

    public void setWhereClausesB(Collection<WhereClause> whereClausesB) {
        this.whereClausesB = whereClausesB;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public RelationMap getRelationMap() {
        return relationMap;
    }

    public void setRelationMap(RelationMap relationMap) {
        this.relationMap = relationMap;
    }

    @ElementCollection
    public Collection<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(Collection<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    @ElementCollection
    public Collection<String> getIncludedColumns() {
        return includedColumns;
    }

    public void setIncludedColumns(Collection<String> includedColumns) {
        this.includedColumns = includedColumns;
    }

    @ElementCollection
    public Collection<String> getExcludedColumns() {
        return excludedColumns;
    }

    public void setExcludedColumns(Collection<String> excludedColumns) {
        this.excludedColumns = excludedColumns;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public Collection<Comparison> getComparisons() {
        return comparisons;
    }

    public void setComparisons(Collection<Comparison> comparisons) {
        this.comparisons = comparisons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelationMapConfig)) return false;
        RelationMapConfig that = (RelationMapConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(configs, that.configs) &&
                Objects.equals(fetch_amount, that.fetch_amount) &&
                Objects.equals(dateFormatA, that.dateFormatA) &&
                Objects.equals(dateFormatB, that.dateFormatB) &&
                Objects.equals(orderDirectionA, that.orderDirectionA) &&
                Objects.equals(orderDirectionB, that.orderDirectionB) &&
                Objects.equals(testTypeA, that.testTypeA) &&
                Objects.equals(testTypeB, that.testTypeB) &&
                Objects.equals(whereClausesA, that.whereClausesA) &&
                Objects.equals(whereClausesB, that.whereClausesB) &&
                Objects.equals(relationMap, that.relationMap) &&
                Objects.equals(primaryKeys, that.primaryKeys) &&
                Objects.equals(includedColumns, that.includedColumns) &&
                Objects.equals(excludedColumns, that.excludedColumns) &&
                Objects.equals(comparisons, that.comparisons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configs, fetch_amount, dateFormatA, dateFormatB, orderDirectionA, orderDirectionB, testTypeA, testTypeB, whereClausesA, whereClausesB, relationMap, primaryKeys, includedColumns, excludedColumns, comparisons);
    }
}
