package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.RaceType;

public class Edge implements Comparable{
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
        String p1County = pair.getElement1().getPrecinct().getCounty();
        String p2County = pair.getElement2().getPrecinct().getCounty();
        if(p1County.equals(p2County)) {
            countyJoinability = 1;
        }else{
            countyJoinability = 0;
        }

        //set raceJoinability
        double p1CommunityRatio = pair.getElement1().getPrecinct().getData().getDemographic().getPercentByRace(community);
        double p2CommunityRatio = pair.getElement2().getPrecinct().getData().getDemographic().getPercentByRace(community);
        raceJoinability = 1-Math.abs(p1CommunityRatio-p2CommunityRatio);

        this.joinability = countyJoinability*this.countyJoinabilityFactor+raceJoinability*this.raceJoinabilityFactor;
    }

    public Cluster getNeighbor(Cluster c){
        return this.pair.getOtherEle(c);
    }

    public void updateEdge(Cluster c1, Cluster c2){
        pair.setElement1(c1);
        pair.setElement2(c2);
    }

    public void updateJoinabilityValue(){

    }

    public Cluster getElement1(){
        return this.getPair().getElement1();
    }

    public Cluster getElement2(){
        return this.getPair().getElement2();
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Edge){
            Edge e2 = (Edge) o;
            if(this.joinability < e2.getJoinability())
                return -1;
            else if(this.joinability > e2.getJoinability())
                return 1;
            else
                return 0;
        }
        else{
            return -1;
        }
    }
}
