package com.example.gerrymanderdemo.model;

public class Range<T extends Comparable<? super T>>{

    private T v1;
    private T v2;

    public Range(T v1, T v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T getV1() {
        return v1;
    }

    public void setV1(T v1) {
        this.v1 = v1;
    }

    public T getV2() {
        return v2;
    }

    public void setV2(T v2) {
        this.v2 = v2;
    }

    public T getLowerBound() {
        return v1.compareTo(v2) < 0 ? v1 : v2;
    }

    public T getUpperBound() {
        return v1.compareTo(v2) > 0 ? v1 : v2;
    }

    public boolean isIncluding(T x) {
        return x.compareTo(getLowerBound()) >= 0 && x.compareTo(getUpperBound()) <= 0;
    }
}
