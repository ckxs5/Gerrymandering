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
import java.util.List;

@Entity
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Data data;
    @OneToMany
    private List<District> districts;
    private StateName name;

    public State() {
    }

    public State(StateName name, List<District> districts) {
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
                new Boundary());
        for (District d : districts) {
            data.add(d.getData());
        }
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
        updateData();
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

    public List<District> getMMDistricts(RaceType community, Range<Double> range) {
        List<District> ds = new ArrayList<>();
        for (District d : districts) {
            if (range.isIncluding(d.getData().getDemographic().getPercentByRace(community))) {
                ds.add(d);
            }
        }
        return ds;
    }


    public StateName getName() {
        return name;
    }

    public void setName(StateName name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getGerrymanderingScore(){
        return 1.0 * Math.abs(this.data.getVoteData().getVote(Party.DEMOCRATIC)
                - this.data.getVoteData().getVote(Party.REPUBLICAN))
                / Math.max(this.data.getVoteData().getVote(Party.DEMOCRATIC),
                this.data.getVoteData().getVote(Party.REPUBLICAN));
    }

    public District getDistrictById(Long id) {
        for(District district : districts) {
            if (district.getId().equals(id)) {
                return district;
            }
        }
        return null;
    }
}
