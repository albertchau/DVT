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
@javax.persistence.Table(name = "dataset")
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @javax.persistence.Column(name = "url", nullable = false)
    private String url;

    @javax.persistence.Column(name = "username", nullable = false)
    private String username;

    public Dataset() {
    }

    public Dataset(String url, String username) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dataset)) {
            return false;
        }

        final Dataset that = (Dataset) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.url, that.url) &&
                Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, username);
    }
}