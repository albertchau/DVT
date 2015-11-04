package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Job;
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
public class JobDAO extends AbstractDAO<Job>{
    public JobDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Job> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Job create(Job job) {
        return persist(job);
    }

    public List<Job> findAll() {
        return list(namedQuery("com.intuit.idea.ziplock.api.core.Job.findAll"));
    }

    public List<Job> findByPersonId(Long aLong) {
        Query query = namedQuery("job.findById2");
        query.setLong("ID", aLong);
        return list(query);
    }
}
