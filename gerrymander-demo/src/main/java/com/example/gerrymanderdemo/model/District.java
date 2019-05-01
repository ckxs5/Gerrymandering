package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.Order;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class District {
    private Data data;
    private List<Precinct> precincts;
    private String id;
    private boolean isMajorityMinority;
    private double tRatio;

    public District(){
        this.precincts = new ArrayList<>();
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(ArrayList<Precinct> precincts) {
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
        this.precincts.add(precinct);
    }

    public Precinct removePrecinct(Precinct precinct){
        Precinct p = null;
        for(int i=0;i<this.precincts.size();i++){
            if(this.precincts.get(i) == precinct){
                p = this.precincts.remove(i);
                break;
            }
        }
        return p;
    }

    public int compareMinorityRatio(double tRatio){

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

    public boolean isMajorityMinorityDistrict(RaceType type, Range range){
        double ratio = (double) this.data.getDemographic().getPopulationByRace(type) / this.data.getDemographic().getPopulationByRace(RaceType.ALL);
        return ratio > range.getMin() && ratio < range.getMax();
    }
}
