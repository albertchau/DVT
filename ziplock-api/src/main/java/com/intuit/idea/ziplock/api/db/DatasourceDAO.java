package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Datasource;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

/**
 * Copyright 2015
 *
 * @author albert
 */
public class DatasourceDAO extends AbstractDAO<Datasource>{
    public DatasourceDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Datasource> findById(Long id) {
        Query query = namedQuery("Datasource.findById");
        return Optional.ofNullable(uniqueResult(query.setLong(0, id)));
    }

    public Datasource create(Datasource datasource) {
        return persist(datasource);
    }

    public List<Datasource> findAll() {
        return list(namedQuery("Datasource.findAll"));
    }

    public Datasource update(Datasource datasource) {
        return persist(datasource);
    }

    public int delete(Long id) {
        return namedQuery("Datasource.deleteById").setLong("ID", id).executeUpdate();
    }
}
