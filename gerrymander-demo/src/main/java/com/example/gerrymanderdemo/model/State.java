package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;

import javax.persistence.Entity;
import java.util.Collection;

public class State {
    private Data data;
    private Collection<Cluster> clusterManager;
    private Collection<District> orgDistricts;
    private Collection<District>districts;
    private int numDistricts;
    private int numMajMinDistricts;
    private String id;

    public State(Data data){

    }

    public State(State s){
        this.data = s.data;
        this.clusterManager = s.clusterManager;  // Todo: Need Deep Clone
        this.orgDistricts = s.orgDistricts;      // Todo: Need Deep Clone
        this.districts = s.districts;            // Todo: Need Deep Clone
        this.numDistricts = s.numDistricts;
        this.numMajMinDistricts = s.numMajMinDistricts;
        this.id = s.id;
    }

    public String getDistrictsBoundary(){
        // Todo
        return null;
    }

    public String getDataAsJSON(){
        // Todo
        return null;
    }

    public double getGerrymanderingScore(){
        // Todo
        return 0;
    }
}
