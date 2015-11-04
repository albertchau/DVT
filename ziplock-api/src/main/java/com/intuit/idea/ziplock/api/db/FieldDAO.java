package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Field;
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
public class FieldDAO extends AbstractDAO<Field>{
    public FieldDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Field> findById(Long id) {
        Query query = namedQuery("Field.findById");
        return Optional.ofNullable(uniqueResult(query.setLong(0, id)));
    }

    public Field create(Field field) {
        return persist(field);
    }

    public List<Field> findAll() {
        return list(namedQuery("Field.findAll"));
    }
}
