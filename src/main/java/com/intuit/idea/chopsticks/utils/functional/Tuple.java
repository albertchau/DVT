package com.intuit.idea.chopsticks.utils.functional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.intuit.idea.chopsticks.utils.functional.TransformerService.convert;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * A tuple is as defined in the relational model, where each element is defined by a name, and is ordered according to the order of elements in the resultant set
 */
public interface Tuple extends Collection<Pair<String, ?>> {
    default <T> Optional<T> val(String name) {
        return (Optional<T>) val(name, Object.class);
    }

    <T> Optional<T> val(int index);

    <T> Optional<T> val(int index, Class<T> type);

    default String asString(String name) {
        return val(name, String.class).orElse("");
    }

    default String asString(int index) {
        return val(index, String.class).orElse("");
    }

    default long asLong(String name) {
        return val(name).map((val) -> convert(val, Long.class)).orElse(0L);
    }

    default float asFloat(String name) {
        return val(name).map((val) -> convert(val, Float.class)).orElse(0.0f);
    }

    default double asDouble(String name) {
        return val(name).map((val) -> convert(val, Double.class)).orElse(0.0);
    }

    default BigDecimal asBigDecimal(String name) {
        return val(name).map((val) -> convert(val, BigDecimal.class)).orElse(null);
    }

    default short asShort(String name) {
        return val(name).map((val) -> convert(val, Short.class)).orElse((short) 0);
    }

    default byte asByte(String name) {
        return val(name).map((val) -> convert(val, Byte.class)).orElse((byte) 0);
    }

    default boolean asBoolean(String name) {
        return val(name).map((val) -> convert(val, Boolean.class)).orElse(false);
    }

    default char asChar(String name) {
        return val(name).map((val) -> convert(val, Character.class)).orElse((char) 0);
    }

    default int asInt(String name) {
        return val(name).map((val) -> convert(val, Integer.class)).orElse(0);
    }

    default int asInt(int index) {
        return val(index).map((val) -> convert(val, Integer.class)).orElse(0);
    }

    default float asFloat(int index) {
        return val(index).map((val) -> convert(val, Float.class)).orElse(0.0f);
    }

    default double asDouble(int index) {
        return val(index).map((val) -> convert(val, Double.class)).orElse(0.0);
    }

    default BigDecimal asBigDecimal(int index) {
        return val(index).map((val) -> convert(val, BigDecimal.class)).orElse(null);
    }

    default long asLong(int index) {
        return val(index).map((val) -> convert(val, Long.class)).orElse(0L);
    }

    default short asShort(int index) {
        return val(index).map((val) -> convert(val, Short.class)).orElse((short) 0);
    }

    default byte asByte(int index) {
        return val(index).map((val) -> convert(val, Byte.class)).orElse((byte) 0);
    }

    default boolean asBoolean(int index) {
        return val(index).map((val) -> convert(val, Boolean.class)).orElse(false);
    }

    default char asChar(int index) {
        return val(index).map((val) -> convert(val, Character.class)).orElse((char) 0);
    }

    default <T> Optional<T> val(String name, Class<T> type) {
        for (Pair<String, ?> stringPair : this) {
            if (name.equalsIgnoreCase(stringPair.getCar())) {
                return ofNullable(convert(stringPair.getCdr(), type));
            }
        }
        return Optional.empty();
    }


    default Tuple subTuple(final String... subset) {
        Collection<Pair<String, ?>> result = stream()
                .filter((pair) -> {
                    for (String s : subset) {
                        if (s.equals(pair.getCar())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(toList());
        return createFrom(result);
    }

    default Tuple merge(Tuple second) {
        Collection<Pair<String, ?>> mergedCollection = new ArrayList<>(this);
        mergedCollection.addAll(this);
        second.forEach((item) -> {
            if (!mergedCollection.contains(item)) {
                mergedCollection.add(item);
            }
        });
        return createFrom(mergedCollection);
    }

    Tuple set(Pair<String, ?>[] columns);

    Tuple reduce(String suffix);

    default boolean hasVal(String name) {
        return val(name).isPresent();
    }

    Tuple createFrom(Collection<Pair<String, ?>> values);
}
