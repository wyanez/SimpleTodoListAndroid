package com.wyanez.simpletodolist.util;

/**
 * Implementation of Interface Consumer de Java 8 for Android.
 * Represents an operation that accepts a input argument and returns no result.
 * Created by william on 10/09/19.
 */

@FunctionalInterface
public interface IConsumerResult<T> {
    public void process(T result);
}
