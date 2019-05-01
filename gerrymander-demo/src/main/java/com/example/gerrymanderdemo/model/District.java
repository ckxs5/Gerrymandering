package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.Order;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.util.Collection;
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
    private boolean isMajorityMinority;
    private double tRatio;

    public District(){
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

    public boolean isMajorityMinority() {
        return isMajorityMinority;
    }

    public void setMajorityMinority(boolean majorityMinority) {
        isMajorityMinority = majorityMinority;
    }

    public double gettRatio() {
        return tRatio;
    }

    public void settRatio(double tRatio) {
        this.tRatio = tRatio;
    }

    public void getNeighbors(){
        // Todo
    }

    public void findBestCandidates(RaceType type, Order order){
        // Todo
    }

    public District testPrecinct(Precinct precinct){
        // Todo
        return null;
    }

    public void addPrecinct(Precinct precinct){
        // Todo
    }

    public Precinct removePrecinct(Precinct precinct){
        // Todo
        return null;
    }

    public int compareMinorityRatio(double tRatio){
        // Todo
        return 0;
    }

    public String getDemoAsJSON(){
        // Todo
        return null;
    }

    public String getBoundary(){
        // Todo
        return null;
    }

    public double isMajorityMinorityDistrict(RaceType type, Range range){
        // Todo
        return 0;
    }
}
