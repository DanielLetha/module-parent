package com.simpletour.biz.traveltrans.bo;

/**
 * Created by Mario on 2015/11/21.
 */
public class Triple<T, U, K> {

    private T first;

    private U second;

    private K third;

    public Triple(T first, U second, K third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public K getThird() {
        return third;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
