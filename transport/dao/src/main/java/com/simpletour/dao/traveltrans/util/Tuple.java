package com.simpletour.dao.traveltrans.util;

/**
 * Created by Mario on 2015/11/21.
 */
public class Tuple<T,U> {

    private T first;

    private U second;

    public Tuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
