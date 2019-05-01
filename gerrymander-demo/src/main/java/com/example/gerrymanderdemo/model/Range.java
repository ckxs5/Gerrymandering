package com.example.gerrymanderdemo.model;

public class Range implements Comparable{

    private double min;
    private double max;

    public Range(double min, double max){
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public boolean isWithin(double n) {
        return (min < n && max > n);
    }

    @Override
    public int compareTo(Object o) {

        return 0;
    }
}
