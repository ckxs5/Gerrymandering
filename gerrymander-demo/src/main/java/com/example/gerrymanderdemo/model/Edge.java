package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.RaceType;

public class Edge {
    //TODO: check if joinability's datatype is ok
    private double joinability;
    private double countyJoinabilityFactor;
    private double raceJoinabilityFactor;
    private Pair pair;

    public Edge(Cluster c1, Cluster c2){
        pair = new Pair(c1,c2);
        countyJoinabilityFactor = 0.5;
        raceJoinabilityFactor = 0.5;
        this.updateJoinabilityValue();
    }

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }

    public double getJoinability() {
        return this.joinability;
    }

    //TODO: change parameter from joinability to community
    public void setJoinability(RaceType community) {
        double countyJoinability;
        double raceJoinability;

        //set countyJoinability
        String pOneCounty = pair.getElement1().getPrecinct().getCounty();
        String pTwoCounty = pair.getElement2().getPrecinct().getCounty();
        if(pOneCounty.equals(pTwoCounty)) {
            countyJoinability = 1;
        }else{
            countyJoinability = 0;
        }

        //set raceJoinability
        double pOneCommunityRatio = pair.getElement1().getPrecinct().getData().getDemographic().getPercentByRace(community);
        double pTwoCommunityRatio = pair.getElement2().getPrecinct().getData().getDemographic().getPercentByRace(community);
        raceJoinability = 1-Math.abs(pOneCommunityRatio-pTwoCommunityRatio);

        this.joinability = countyJoinability*this.countyJoinabilityFactor+raceJoinability*this.raceJoinabilityFactor;
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
