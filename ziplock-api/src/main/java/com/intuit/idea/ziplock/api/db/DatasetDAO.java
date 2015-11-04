package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Dataset;
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
public class DatasetDAO extends AbstractDAO<Dataset>{
    public DatasetDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Dataset> findById(Long id) {
        Query query = namedQuery("Dataset.findById");
        return Optional.ofNullable(uniqueResult(query.setLong(0, id)));
    }

    public Dataset create(Dataset dataset) {
        return persist(dataset);
    }

    public List<Dataset> findAll() {
        return list(namedQuery("Dataset.findAll"));
    }

    public Dataset update(Dataset updateDataset) {
        return persist(updateDataset);
    }

    public Integer delete(Long id) {
        return namedQuery("Dataset.deleteById")
                .setLong("ID", id)
                .executeUpdate();
    }
}
