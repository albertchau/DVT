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
                name = "Relation.findAll",
                query = "SELECT t FROM Relation t"
        ),
        @NamedQuery(
                name = "Relation.findById",
                query = "SELECT t FROM Relation t"
        ),
        @NamedQuery(
                name = "Relation.findByDatasourceId",
                query = "SELECT distinct relations from Datasource as d left join d.relations as relations where d.id = :ID"
        )
})
public class Relation {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String name;
    @Basic
    private String query;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Relation)) return false;
        Relation relation = (Relation) o;
        return Objects.equals(id, relation.id) &&
                Objects.equals(name, relation.name) &&
                Objects.equals(query, relation.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, query);
    }
}
