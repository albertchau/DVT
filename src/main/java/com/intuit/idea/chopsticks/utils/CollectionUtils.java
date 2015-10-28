package com.intuit.idea.chopsticks.utils;

import java.util.Collection;
import java.util.Map;

/**
 * ************************************
 * Author: achau1
 * Created On: 10/7/15
 * ************************************
 */
public class CollectionUtils {
    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNullOrEmpty(final Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    public static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
