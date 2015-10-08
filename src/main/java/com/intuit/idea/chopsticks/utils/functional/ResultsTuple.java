package com.intuit.idea.chopsticks.utils.functional;

import com.intuit.idea.chopsticks.utils.exceptions.DataAccessException;

import java.util.*;

import static com.intuit.idea.chopsticks.utils.functional.Pair.cons;
import static com.intuit.idea.chopsticks.utils.functional.TransformerService.convert;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * implementation of a Tuple to represent a ResultSet. It stores the values internally in an Pair[]
 */
public class ResultsTuple extends AbstractList<Pair<String, ?>> implements Tuple {

    //note the optional is untyped which makes tuples easier to work with rather than using wildcards
    Pair<String, ?>[] list;

    public ResultsTuple(Pair<String, ?>... list) {
        this.list = list;
    }

    public ResultsTuple(Collection<Pair<String, ?>> values) {
        this.list = new Pair[values.size()];
        int cnt = 0;
        for (Pair<String, ?> value : values) {
            this.list[cnt++] = value;
        }
    }

    public static Tuple tuple(Pair<String, ?>... list) {
        return new ResultsTuple(list);
    }

    public static Pair<String, ?> tCons(String name, Object value) {
        return cons(name, value);
    }

    @Override
    public <T> Optional<T> val(String name) {
        return (Optional<T>) val(name, Object.class);
    }

    @Override
    public <T> Optional<T> val(int index) {
        if (index > -1 && index < list.length)
            throw new DataAccessException("Out of index");
        return (Optional<T>) val(index, Object.class);
    }

    @Override
    public <T> Optional<T> val(int index, Class<T> type) {
        if (index > -1 && index < list.length)
            throw new DataAccessException("Out of index");
        if (type.isAssignableFrom(list[1].getCdr().getClass()))
            throw new DataAccessException("Cannot get idx: " + index + " as specified data getType: " + type.toGenericString());
        T casted = type.cast(list[index].getCdr());
        return ofNullable(casted);
    }

    @Override
    public <T> Optional<T> val(String name, Class<T> type) {
        for (Pair<String, ?> stringPair : list) {
            if (name.equalsIgnoreCase(stringPair.getCar())) {
                return ofNullable(convert(stringPair.getCdr(), type));
            }
        }
        return Optional.empty();
    }

    @Override
    public Tuple set(Pair<String, ?>[] columns) {
        ResultsTuple newTuple = new ResultsTuple(columns);
        return newTuple.merge(this);
    }

    @Override
    public Tuple reduce(String suffix) {
        List<Pair<String, ?>> results = stream().filter((column) -> column.getCar().toLowerCase().startsWith(suffix.toLowerCase())).collect(toList());
        return new ResultsTuple(results);
    }

    @Override
    public Tuple createFrom(Collection<Pair<String, ?>> values) {
        return new ResultsTuple(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;
        if (!super.equals(o)) return false;

        ResultsTuple pairs = (ResultsTuple) o;

        return Arrays.equals(list, pairs.list);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (list != null ? Arrays.hashCode(list) : 0);
        return result;
    }

    @Override
    public Pair<String, ?> get(int index) {
        return list[index];
    }

    @Override
    public int size() {
        return list.length;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("(");
        forEach((Pair<String, ?> pair) -> builder.append(pair.toString()).append(','));
        return builder.deleteCharAt(builder.length() - 1).append(')').toString();
    }
}