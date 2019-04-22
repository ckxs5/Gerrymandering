package com.example.gerrymanderdemo.model;

public class Edge {
    private float joinability;
    private Pair pair;

    public Edge(Cluster c1, Cluster c2){
        pair = new Pair(c1,c2);

        this.updateJoinabilityValue();
    }

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }

    public float getJoinability() {
        return joinability;
    }

    public void setJoinability(float joinability) {
        this.joinability = joinability;
    }

    public Cluster getNeighbor(Cluster c){
        // Todo
        return null;
    }

    public void updateEdge(Cluster c1, Cluster c2){
        // Todo
    }

    public void updateJoinabilityValue(){
        // Todo
    }

    public Cluster getElement1(){
        return this.getPair().getElement1();
    }

    public Cluster getElement2(){
        return this.getPair().getElement2();
    }
}
