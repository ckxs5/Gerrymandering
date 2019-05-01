package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Properties;
import com.example.gerrymanderdemo.model.Enum.JoinabilityMeasureType;
import com.example.gerrymanderdemo.model.Enum.RaceType;

import java.util.HashSet;
import java.util.Set;

public class Edge extends Pair<Cluster> implements Comparable{
    //TODO: check if joinability's datatype is ok
    private double joinability;
    private RaceType communityOfInterest;

    public Edge(Cluster ele1, Cluster ele2, RaceType communityOfInterest) {
        super(ele1, ele2);
        this.communityOfInterest = communityOfInterest;
        this.setJoinability();
    }

    @Override
    public void setElement1(Cluster element1) {
        super.setElement1(element1);
        setJoinability();
    }

    @Override
    public void setElement2(Cluster element2) {
        super.setElement2(element2);
        setJoinability();
    }

    @Override
    public boolean updateElment(Cluster orgE, Cluster newE) {
        if (super.updateElment(orgE, newE)) {
            setJoinability();
            return true;
        }
        return false;
    }

    public RaceType getCommunityOfInterest() {
        return communityOfInterest;
    }

    public void setCommunityOfInterest(RaceType communityOfInterest) {
        this.communityOfInterest = communityOfInterest;
        setJoinability();
    }

    public double getJoinability() {
        return this.joinability;
    }

    public void setJoinability() {
        this.joinability = JoinabilityMeasureType.values().length / getTotalJoinability();
    }

    private double getTotalJoinability() {
        double sum = 0;
        for (JoinabilityMeasureType measureType: JoinabilityMeasureType.values()) {
            int rate = Properties.getJoinabilityMeasureRatio(measureType);
            double val = 0;
            switch (measureType) {
                case RACE_JOINABILITY:
                    val = getRaceJoinability() ;
                    break;
                case COUNTY_JOINABILITY:
                    val = getCountyJoinability();
                    break;
                default:
                    break;
            }
            sum += val * rate;
        }
        return sum;
    }

    private double getCountyJoinability() {
        Set<String> e1County = this.getElement1().getCounties();
        Set<String> e2County = this.getElement2().getCounties();
        Set<String> intersection = new HashSet<>(e1County);
        intersection.retainAll(e2County);
        return (double) intersection.size() / Math.min(e1County.size(), e2County.size());
    }

    private double getRaceJoinability() {
        double p1CommunityRatio = this.getElement1().getData().getDemographic().getPercentByRace(communityOfInterest);
        double p2CommunityRatio = this.getElement2().getData().getDemographic().getPercentByRace(communityOfInterest);
        return  (1 - Math.abs(p1CommunityRatio - p2CommunityRatio)) / 1;
    }

    public boolean isRedundant() {
        return getElement1().equals(getElement2());
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Edge){
            Edge e2 = (Edge) o;
            return Double.compare(this.joinability, e2.getJoinability());
        }
        else{
            return -1;
        }
    }
}
