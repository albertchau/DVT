package com.intuit.idea.chopsticks.query;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ************************************
 * Author: achau1
 * Created On: 9/20/15
 * ************************************
 */
public class WhereClause<T extends Comparable> {

    private final T lowerBound;
    private final T upperBound;
    private final String column;
    private final T equality;
    private final List<T> inBounds;
    private final String customWhere;

    protected WhereClause(T lowerBound, T upperBound, String column, T equality, List<T> inBounds, String customWhere) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.column = column;
        this.equality = equality;
        this.inBounds = inBounds;
        this.customWhere = customWhere;
    }

    public static <U extends Comparable> WhereClause createBounded(U lowerBound, U upperBound, String column) {
        return new WhereClause<>(lowerBound, upperBound, column, null, null, null);
    }

    public static <U extends Comparable> WhereClause createUpperBounded(U upperBound, String column) {
        return new WhereClause<>(null, upperBound, column, null, null, null);
    }

    public static <U extends Comparable> WhereClause createLowerBounded(U lowerBound, String column) {
        return new WhereClause<>(lowerBound, null, column, null, null, null);
    }

    public static <U extends Comparable> WhereClause createInSet(List<U> inBounded, String column) {
        return new WhereClause<>(null, null, column, null, inBounded, null);
    }

    public static <U extends Comparable> WhereClause createEqual(U equality, String column) {
        return new WhereClause<>(null, null, column, equality, null, null);
    }

    public static <U extends Comparable> WhereClause createCustom(String customWhere) {
        return new WhereClause<>(null, null, null, null, null, customWhere);
    }

    public Class<?> type() {
        if (lowerBound != null)
            return lowerBound.getClass();
        if (upperBound != null)
            return upperBound.getClass();
        if (equality != null)
            return equality.getClass();
        if (inBounds != null && !inBounds.isEmpty())
            return inBounds.get(0).getClass();
        return null;
    }

    public String getColumn() {
        return column;
    }

    public String constructClause(QueryService qs) {
        if (customWhere != null) {
            return customWhere;
        }
        Class type = type();
        if (type.equals(String.class)) {
            if (equality != null) {
                return column + " = '" + equality + "'";
            }
            if (inBounds != null) {
                String joinedIn = inBounds.stream().map(s -> (String) s).collect(Collectors.joining("','"));
                return column + " IN ('" + joinedIn + "')";
            }
            return null;
        } else if (type.equals(Integer.class) || type.equals(Long.class)) {
            if (equality != null) {
                return column + " = " + equality;
            }
            if (inBounds != null) {
                String joinedIn = inBounds.stream().map(i -> Integer.toString((Integer) i)).collect(Collectors.joining(","));
                return column + " IN (" + joinedIn + ")";
            }
            List<String> tmp = new ArrayList<>();
            if (lowerBound != null) {
                tmp.add(column + " > " + lowerBound);
            }
            if (upperBound != null) {
                tmp.add(column + " < " + upperBound);
            }
            return tmp.stream().collect(Collectors.joining(" AND "));
        } else if (type.equals(DateTime.class)) {
            DateTime startDate = null;
            DateTime endDate = null;
            if (lowerBound != null) {
                startDate = (DateTime) lowerBound;
            }
            if (upperBound != null) {
                endDate = (DateTime) upperBound;
            }
            return qs.getDateRange(column, startDate, endDate);
        } else {
            if (equality != null) {
                return column + " = " + equality;
            }
            if (inBounds != null) {
                String joinedIn = inBounds.stream().map(s -> (String) s).collect(Collectors.joining("','"));
                return column + " IN ('" + joinedIn + "')";
            }
            List<String> tmp = new ArrayList<>();
            if (lowerBound != null) {
                tmp.add(column + " > " + lowerBound);
            }
            if (upperBound != null) {
                tmp.add(column + " < " + upperBound);
            }
            return tmp.stream().collect(Collectors.joining(" AND "));
        }
    }

    public T getLowerBound() {
        return lowerBound;
    }

    public T getUpperBound() {
        return upperBound;
    }

    @Override
    public String toString() {
        return "WhereClause{" +
                "lowerBound=" + lowerBound +
                ", upperBound=" + upperBound +
                ", column='" + column + '\'' +
                ", equality=" + equality +
                ", inBounds=" + inBounds +
                ", customWhere='" + customWhere + '\'' +
                '}';
    }
}
