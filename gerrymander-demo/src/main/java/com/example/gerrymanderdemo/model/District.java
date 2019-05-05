package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.Order;
import com.example.gerrymanderdemo.model.Enum.RaceType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 100)
    private String id;
    @OneToOne
    private Data data;
    @OneToMany
    private Set<Precinct> precincts;

    public District(){
    }

    public District(District dist){
        this.id = dist.getId();
        this.data = new Data(dist.getData());
        this.precincts = new HashSet<>();
        this.precincts.addAll(dist.getPrecincts());
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Precinct> getNeighbors(){
        Set<Precinct> neighbours = new HashSet<>();
        for (Precinct p: precincts) {
            neighbours.addAll(p.getNeighbors());
        }
        return neighbours;
    }

    public Precinct findBestCandidate(RaceType type, Order order){
        Precinct minCandidate = (Precinct) precincts.toArray()[0];
        Precinct maxCandidate = (Precinct) precincts.toArray()[0];

        for (Precinct p : precincts) {
            if (minCandidate.getData().getDemographic().getPercentByRace(type)
                    > p.getData().getDemographic().getPercentByRace(type)) {
                minCandidate = p;
            }
            if (maxCandidate.getData().getDemographic().getPercentByRace(type)
                    < p.getData().getDemographic().getPercentByRace(type)) {
                maxCandidate = p;
            }
        }

        switch (order) {
            case ASCENDING:
                return minCandidate;
            case DESCENDING:
                return maxCandidate;
            default:
                return null;
        }
    }

    public District testPrecinct(Precinct precinct){
        //TODO: This can only work when deep copy is implemented correctly
        District result = new District(this);
        result.addPrecinct(precinct);
        return result;
    }

    public void addPrecinct(Precinct precinct){
        if (precincts.add(precinct)) {
            this.data.add(precinct.getData());
        }
    }

    public Precinct removePrecinct(Precinct precinct){
        if (precincts.remove(precinct)) {
            this.data.remove(precinct.getData());
            return precinct;
        }
        return null;
    }

    public double compareMinorityRatio(RaceType communityOfInterest, Range<Double> tRatio){
        double actual = this.getData().getDemographic().getPercentByRace(communityOfInterest);
        if (tRatio.getUpperBound() < actual) {
            return actual - tRatio.getUpperBound();
        } else if (tRatio.getLowerBound() > actual) {
            return tRatio.getLowerBound() - actual;
        } else {
            return 0;
        }
    }

    public boolean isMajorityMinority(RaceType communityOfInterest, Range range){
        return range.isIncluding(this.getData().getDemographic().getPercentByRace(communityOfInterest));
    }
}
