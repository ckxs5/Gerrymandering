package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
public class Data implements ResponseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Vote voteData;
    @OneToOne
    private Demographic demographic;
    @OneToOne
    private Boundary boundary;

    public Data() {
    }

    public Data(Data data) {
        this.voteData = new Vote(data.getVoteData());
        this.demographic = new Demographic(data.getDemographic());
        this.boundary = new Boundary(data.getBoundary());
    }

    public Data(Data d1, Data d2) {
        this.voteData = new Vote(d1.getVoteData(), d2.getVoteData());
        this.demographic = new Demographic(d1.getDemographic(), d2.getDemographic());
        this.boundary = new Boundary(d1.getBoundary(), d2.getBoundary());
    }

    public Data(Vote voteData, Demographic demographic, Boundary boundary) {
        this.voteData = voteData;
        this.demographic = demographic;
        this.boundary = boundary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vote getVoteData() {
        return voteData;
    }

    public void setVoteData(Vote voteData) {
        this.voteData = voteData;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public void add(Data other) {
        this.voteData.add(other.getVoteData());
        this.demographic.add(other.getDemographic());
        this.boundary.add(other.getBoundary());
    }

    public Data remove(Data other) {
        this.voteData.remove(other.getVoteData());
        this.demographic.remove(other.getDemographic());
        this.boundary.remove(other.getBoundary());
        return other;
    }

    @Override
    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("demographic", demographic.toJSONObject());
            json.put("votingData", voteData.toJSONObject());
            return json;
        } catch (JSONException ex) {
            System.out.println("Unexpected error occurs when converting Data into JSON object");
            return null;
        }
    }
}
