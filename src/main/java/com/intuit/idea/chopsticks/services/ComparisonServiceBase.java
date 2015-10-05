package com.intuit.idea.chopsticks.services;

import com.intuit.idea.chopsticks.query.Metadata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Copyright 2015
 *
 * @author albert
 */
public abstract class ComparisonServiceBase implements ComparisonService {
    /*
        Syntactic sugar for a functional lambda call to sort the list of comparable[]. Also a wrapper for overloaded compareColumns(). todo candidate for abstraction
         */
    protected static Integer compareColumns(Comparable[] source, Comparable[] target) {
        return compareColumns(source, target, filledBiFunctionArray(source.length), 0, source.length);
    }

    /*
        Just fills an array of Comparable.compareTo() BiFunctions to the size specified. todo candidate for abstraction
         */
    @SuppressWarnings("unchecked")
    private static BiFunction<Comparable, Comparable, Integer>[] filledBiFunctionArray(int size) {
        BiFunction<Comparable, Comparable, Integer>[] comparers = new BiFunction[size];
        Arrays.fill(comparers, ExistenceComparisonService.comparer);
        return comparers;
    }

    /*
        Syntactic sugar for functional lambda that retrieves a column value and casts the value to the type specified in the metadata. todo candidate for abstraction
         */
    protected static Function<Metadata, Comparable> getColumnValue(ResultSet rs) {
        return (md) -> {
            try {
                return md.getType().cast(rs.getObject(md.getColumn(), md.getType()));
            } catch (ClassCastException | SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }

    /*
        Compares the range of rows in array (specified by inclusive start and exclusive end) todo candidate for abstraction
         */
    protected static Integer compareColumns(Comparable[] sRow, Comparable[] tRow, BiFunction<Comparable, Comparable, Integer>[] comparers, int start, int end) {
        for (int i = start; i < end; i++) {
            int compareVal = comparers[i].apply(sRow[i], tRow[i]);
            if (compareVal != 0) {
                return compareVal;
            }
        }
        return 0;
    }
}
