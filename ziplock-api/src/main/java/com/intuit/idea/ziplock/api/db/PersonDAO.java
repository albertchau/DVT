package com.intuit.idea.ziplock.api.db;

import com.intuit.idea.ziplock.api.core.Person;
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
public class PersonDAO extends AbstractDAO<Person>{
    public PersonDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Person> findById(Long id) {
        Query query = namedQuery("person.findById");
        return Optional.ofNullable(uniqueResult(query.setLong("ID", id)));
    }

    public Person create(Person person) {
        return persist(person);
    }

    public List<Person> findAll() {
        return list(namedQuery("person.findAll"));
    }
}
