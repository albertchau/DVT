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
                name = "Datasource.findAll",
                query = "SELECT ds FROM Datasource ds"
        ),
        @NamedQuery(
                name = "Datasource.deleteById",
                query = "delete Datasource where id = :ID"
        )
})
public class Datasource {
    @GeneratedValue
    @Id
    private Long id;
    @Basic
    private String name;
    @Basic
    private String host;
    @Basic
    private String port;
    @Basic
    private String url;
    @Basic
    private String username;
    @Basic
    private String password;
    @Basic
    private String database_schema;
    @Basic
    private String hive_principal_queue;
    @ManyToOne(cascade = CascadeType.ALL)
    private DatasourceType datasourceType;
    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Datasource> shards;
    @OneToMany(cascade = CascadeType.ALL)
    private Collection<Relation> relations;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase_schema() {
        return database_schema;
    }

    public void setDatabase_schema(String database_schema) {
        this.database_schema = database_schema;
    }

    public String getHive_principal_queue() {
        return hive_principal_queue;
    }

    public void setHive_principal_queue(String hive_principal_queue) {
        this.hive_principal_queue = hive_principal_queue;
    }

    public DatasourceType getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(DatasourceType datasourceType) {
        this.datasourceType = datasourceType;
    }

    public Collection<Datasource> getShards() {
        return shards;
    }

    public void setShards(Collection<Datasource> shards) {
        this.shards = shards;
    }

    public Collection<Relation> getRelations() {
        return relations;
    }

    public void setRelations(Collection<Relation> relations) {
        this.relations = relations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Datasource)) return false;
        Datasource that = (Datasource) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(host, that.host) &&
                Objects.equals(port, that.port) &&
                Objects.equals(url, that.url) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(database_schema, that.database_schema) &&
                Objects.equals(hive_principal_queue, that.hive_principal_queue) &&
                Objects.equals(datasourceType, that.datasourceType) &&
                Objects.equals(shards, that.shards) &&
                Objects.equals(relations, that.relations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, host, port, url, username, password, database_schema, hive_principal_queue, datasourceType, shards, relations);
    }
}
