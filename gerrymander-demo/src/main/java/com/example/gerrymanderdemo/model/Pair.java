package com.example.gerrymanderdemo.model;

import java.util.Objects;

public class Pair<T> {

    private T element1;
    private T element2;

    public Pair(T ele1, T ele2){
        this.element1 = ele1;
        this.element2 = ele2;
    }

    public T getElement1() {
        return element1;
    }

    public void setElement1(T element1) {
        this.element1 = element1;
    }

    public T getElement2() {
        return element2;
    }

    public void setElement2(T element2) {
        this.element2 = element2;
    }

    public T getTheOther(T e){
        return this.element1.equals(e) ? this.element2: this.element1;
    }

    public boolean updateElement (T orgE, T newE) {
        boolean done = false;
        if (element1.equals(orgE)) {
            setElement1(newE);
            done = true;
        } else if (element2.equals(orgE)) {
            setElement2(newE);
            done = true;
        }
        return done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?> pair = (Pair<?>) o;
        return getElement1().equals(pair.getElement1()) && getElement2().equals(pair.getElement2())
                || getElement2().equals(pair.getElement1()) && getElement1().equals(pair.getElement2());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getElement1(), getElement2(), getElement2(), getElement1());
    }
}
