package com.example.gerrymanderdemo.model;

public class Pair {

    private Cluster element1;
    private Cluster element2;

    public Pair(Cluster ele1, Cluster ele2){
        this.element1 = ele1;
        this.element2 = ele2;
    }

    public Cluster getElement1() {
        return element1;
    }

    public void setElement1(Cluster element1) {
        this.element1 = element1;
    }

    public Cluster getElement2() {
        return element2;
    }

    public void setElement2(Cluster element2) {
        this.element2 = element2;
    }

    public Pair getPair(){
        return this;
    }

    public Cluster getOtherEle(Cluster ele){
        return (this.element1 == ele)?this.element1:this.element2;
    }

}
