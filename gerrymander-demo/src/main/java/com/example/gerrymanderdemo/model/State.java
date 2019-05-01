package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;

import javax.persistence.Entity;
import java.util.Collection;

public class State {
    private Data data;
    private Collection<Cluster> clusterManager;
    private Collection<District> orgDistricts;
    private Collection<District>districts;
    private int numDistricts;
    //TODO: See Whether if we need this?
    private int numMajMinDistricts;
    private StateName name;
    private String id;
    private int idealClusterPop;    //TODO
    private double districtPopulationVariant = 1.2;     //TODO: How to get it from properties file

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Collection<Cluster> getClusterManager() {
        return clusterManager;
    }

    public void setClusterManager(Collection<Cluster> clusterManager) {
        this.clusterManager = clusterManager;
    }

    public Collection<District> getOrgDistricts() {
        return orgDistricts;
    }

    public void setOrgDistricts(Collection<District> orgDistricts) {
        this.orgDistricts = orgDistricts;
    }

    public Collection<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Collection<District> districts) {
        this.districts = districts;
    }

    public int getNumDistricts() {
        return numDistricts;
    }

    public void setNumDistricts(int numDistricts) {
        this.numDistricts = numDistricts;
    }

    public int getNumMajMinDistricts(RaceType community, Range<Double> range) {
        int count = 0;
        for (District d : districts) {
            if (range.isIncluding(d.getData().getDemographic().getPercentByRace(community))) {
                count++;
            }
        }
        return count;
    }

    public void setNumMajMinDistricts(int numMajMinDistricts) {
        this.numMajMinDistricts = numMajMinDistricts;
    }

    public StateName getName() {
        return name;
    }

    public void setName(StateName name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdealClusterPop(int idealClusterPop) {
        this.idealClusterPop = idealClusterPop;
    }

    public double getDistrictPopulationVariant() {
        return districtPopulationVariant;
    }

    public void setDistrictPopulationVariant(double districtPopulationVariant) {
        this.districtPopulationVariant = districtPopulationVariant;
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

    //TODO:check
    public int getIdealClusterPop(){
        int totalPop = data.getDemographic().getPopulationByRace(RaceType.ALL);
        idealClusterPop = totalPop/numDistricts;
        return idealClusterPop;
    }
}
