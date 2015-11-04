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
@Table(name = "config")
@NamedQueries({
        @NamedQuery(
                name = "Config.findAll",
                query = "SELECT r FROM Config r"
        )
})
public class Config {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "username", nullable = false)
    private String username;

    public Config() {
    }

    public Config(String url, String username) {
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
        if (!(o instanceof Config)) {
            return false;
        }

        final Config that = (Config) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.url, that.url) &&
                Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, username);
    }
}