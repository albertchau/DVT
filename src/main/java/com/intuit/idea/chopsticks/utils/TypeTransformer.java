package com.intuit.idea.chopsticks.utils;

/**
 * Functional interface for transforming an instance from getType T to K
 */

@FunctionalInterface
public interface TypeTransformer<T, K> {
    K transform(T instance);
}
