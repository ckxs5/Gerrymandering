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

import java.util.Collection;

@Controller
public class District {
    private Data data;
    private Collection<Precinct> precincts;
    private String id;
    private boolean isMajorityMinority;
    private double tRatio;

    public District(){
        // Todo
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Collection<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Collection<Precinct> precincts) {
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
