package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Precinct implements ResponseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 100)
    private String id;
    private String name;
    //TODO: extract the county this precinct belongs to,  will be used for countyJoinability in Edge
    private String county;
    @OneToOne
    private Data data;
    @ManyToMany
    @JoinTable(
            name = "precinct_precinct",
            joinColumns = @JoinColumn(name = "precinct1_id"),
            inverseJoinColumns = @JoinColumn(name = "precinct2_id"))
    private Set<Precinct> neighbors;

    public Precinct() {
    }

    public Precinct(Precinct precinct) {
        this.name = precinct.getName();
        this.county = precinct.getCounty();
        this.data = new Data(precinct.getData());
        this.neighbors = precinct.getNeighbors();
    }

    public Precinct(String id, String name, Data data, Set<Precinct> neighbors) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.neighbors = neighbors;
    }

    public String getCounty(){ return county; }

    public void setCounty(String county){ this.county = county; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Set<Precinct> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Set<Precinct> neighbors) {
        this.neighbors = neighbors;
    }

    public String getBoundary() {
        return data.getBoundary().toJSONObject().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJSONObject() {
        try{
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("data", data.toJSONObject());
            //TODO: put neighbours into json
            return json;
        } catch (JSONException ex) {
            System.out.println("Unexpected error occurs when converting precinct to JSON object");
            return null;
        }
    }

}
