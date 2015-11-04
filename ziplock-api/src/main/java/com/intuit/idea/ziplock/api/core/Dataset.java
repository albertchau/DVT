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
                name = "Dataset.findAll",
                query = "SELECT ds FROM Dataset ds"
        ),
        @NamedQuery(
                name = "Dataset.deleteById",
                query = "delete Dataset where id = :ID"
        )
})
public class Dataset {
    private Long id;
    private String name;
    private Datasource datasourceA;
    private Datasource datasourceB;

    @GeneratedValue
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Datasource getDatasourceA() {
        return datasourceA;
    }

    public void setDatasourceA(Datasource datasourceA) {
        this.datasourceA = datasourceA;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Datasource getDatasourceB() {
        return datasourceB;
    }

    public void setDatasourceB(Datasource datasourceB) {
        this.datasourceB = datasourceB;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dataset)) return false;
        Dataset dataset = (Dataset) o;
        return Objects.equals(id, dataset.id) &&
                Objects.equals(name, dataset.name) &&
                Objects.equals(datasourceA, dataset.datasourceA) &&
                Objects.equals(datasourceB, dataset.datasourceB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, datasourceA, datasourceB);
    }
}
