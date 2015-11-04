package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Reporter;
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
public class ReporterDAO extends AbstractDAO<Reporter>{
    public ReporterDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Reporter> findById(Long id) {
        Query query = namedQuery("Reporter.findById");
        return Optional.ofNullable(uniqueResult(query.setLong(0, id)));
    }

    public Reporter create(Reporter reporter) {
        return persist(reporter);
    }

    public List<Reporter> findAll() {
        return list(namedQuery("Reporter.findAll"));
    }
}
