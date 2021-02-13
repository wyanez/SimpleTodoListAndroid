package com.wyanez.simpletodolist.util;

/**
 * Implementation of Interface Consumer de Java 8 for Android.
 * Represents an operation that accepts a input argument and returns no result.
 * java.util.funciton.Consumer requires API min 24, current 21
 * Created by william on 10/09/19.
 */

@FunctionalInterface
public interface IConsumerResult<T> {
    void process(T result);
}
