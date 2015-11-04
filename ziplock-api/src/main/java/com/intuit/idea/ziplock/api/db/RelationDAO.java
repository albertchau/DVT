package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Relation;
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
public class RelationDAO extends AbstractDAO<Relation>{
    public RelationDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Relation> findById(Long id) {
        Query query = namedQuery("Relation.findById");
        return Optional.ofNullable(uniqueResult(query.setLong(0, id)));
    }

    public Relation create(Relation relation) {
        return persist(relation);
    }

    public List<Relation> findAll() {
        return list(namedQuery("Relation.findAll"));
    }

    public List<Relation> findRelationsByDatasourceId(Long id) {
        Query query = namedQuery("Relation.findByDatasourceId")
                .setLong("ID", id);
        return list(query);
    }
}
