package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Set;


@Entity
public class Precinct implements ResponseObject {
    @Id
    private Long id;
    private String name;
    private StateName state;
    //TODO: extract the county this precinct belongs to,  will be used for countyJoinability in Edge
    private String county;
    @OneToOne(fetch = FetchType.EAGER)
    private Data data;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "precinct_precinct",
            joinColumns = @JoinColumn(name = "precinct1_id"),
            inverseJoinColumns = @JoinColumn(name = "precinct2_id"))
    private Set<Precinct> neighbors;

    private long districtId;

    public Precinct() {
    }
    
    public Precinct(Long id, String name, Data data, Set<Precinct> neighbors) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.neighbors = neighbors;
    }

    public void setDistrictId(long districtId){
        this.districtId = districtId;
    }

    public Long getDistrictId(){ return districtId; }

    public String getCounty(){ return county; }

    public void setCounty(String county){ this.county = county; }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StateName getState() {
        return state;
    }

    public void setState(StateName state) {
        this.state = state;
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

    public double[][] getBoundary() {
        return data.getBoundary().getGeoJSON();
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
    //TODO
    public Set<String> getNeighborIDs() {
        return null;
    }

    @Override
    public String toString() {
        return "Precinct{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", county='" + county + '\'' +
                ", data=" + data +
                ", neighbors=" + neighbors.size() +
                '}';
    }
}
