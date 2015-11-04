package com.intuit.idea.ziplock.api.core;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/29/15
 * ************************************
 */

@Entity
@Table(name = "datasource")
@NamedQueries({
        @NamedQuery(
                name = "Datasource.findAll",
                query = "SELECT ds FROM Datasource ds"
        ),
        @NamedQuery(
                name = "Datasource.deleteById",
                query = "delete Datasource where id = :ID"
        )
})
public class Datasource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @javax.persistence.Column(name = "url", nullable = false)
    private String url;

    @javax.persistence.Column(name = "username", nullable = false)
    private String username;

    @OneToMany
    private List<Relation> relations = new ArrayList<>();

    public Datasource() {
    }

    public Datasource(String url, String username) {
        this.url = url;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Datasource)) {
            return false;
        }

        final Datasource that = (Datasource) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.url, that.url) &&
                Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, username);
    }
}