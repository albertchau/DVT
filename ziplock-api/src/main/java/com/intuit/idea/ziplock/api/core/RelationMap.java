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
                name = "RelationMap.findAll",
                query = "SELECT r FROM RelationMap r"
        )
})
public class RelationMap {
    @GeneratedValue
    @Id
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Relation relationA;
    @ManyToOne(cascade = CascadeType.ALL)
    private Relation relationB;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "relationMap")
    private Collection<RelationMapConfig> configsFromThisMap;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Relation getRelationA() {
        return relationA;
    }

    public void setRelationA(Relation relationA) {
        this.relationA = relationA;
    }

    public Relation getRelationB() {
        return relationB;
    }

    public void setRelationB(Relation relationB) {
        this.relationB = relationB;
    }

    public Collection<RelationMapConfig> getConfigsFromThisMap() {
        return configsFromThisMap;
    }

    public void setConfigsFromThisMap(Collection<RelationMapConfig> configsFromThisMap) {
        this.configsFromThisMap = configsFromThisMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelationMap)) return false;
        RelationMap that = (RelationMap) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(relationA, that.relationA) &&
                Objects.equals(relationB, that.relationB) &&
                Objects.equals(configsFromThisMap, that.configsFromThisMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, relationA, relationB, configsFromThisMap);
    }
}
