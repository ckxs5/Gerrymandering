package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Boundary;
import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 100)
    private String id;
    @OneToOne
    private Data data;
    @OneToMany
    private Collection<District>districts;
    private int numDistricts;
    //TODO: See Whether if we need this?
    private int numMajMinDistricts;
    private StateName name;
    @Value("${gerrymandering.range.variant}")
    private double districtPopulationVariant;

    public State() {
    }

    public State(StateName name, Collection<District> districts) {
        this.name = name;
        this.districts = districts;
        updateData();
    }

    public State(State s){
        this.data = new Data(s.getData());
        this.districts = new ArrayList<>();// Todo: Need Deep Clone
        for(District d : s.getDistricts()) {
            districts.add(new District());
        }
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

    private void updateData(){
        data = new Data(new Vote(new int[Party.values().length]),
                new Demographic(new int[RaceType.values().length]),
                new Boundary(""));
        for (District d : districts) {
            data.add(d.getData());
        }
    }

    public Collection<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Collection<District> districts) {
        this.districts = districts;
        updateData();
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

    public double getGerrymanderingScore(){
        return 1.0 * Math.abs(this.data.getVoteData().getVote(Party.DEMOCRATIC)
                - this.data.getVoteData().getVote(Party.REPUBLICAN))
                / Math.max(this.data.getVoteData().getVote(Party.DEMOCRATIC),
                this.data.getVoteData().getVote(Party.REPUBLICAN));
    }
    //TODO: Change districts type to HashMap<districtId, district>
    public District getDistrict(String s) {
        return null;
    }
}
