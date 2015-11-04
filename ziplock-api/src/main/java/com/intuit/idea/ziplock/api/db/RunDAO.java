package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Run;
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
public class RunDAO extends AbstractDAO<Run>{
    public RunDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Run> findById(Long id) {
        Query query = namedQuery("Run.findById");
        return Optional.ofNullable(uniqueResult(query.setLong(0, id)));
    }

    public Run create(Run run) {
        return persist(run);
    }

    public List<Run> findAll() {
        return list(namedQuery("Run.findAll"));
    }
}
