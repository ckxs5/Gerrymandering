package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

public class Data implements ResponseObject {
    Vote voteData;
    Demographic demographic;
    Boundary boundary;

    public Data(Vote voteData, Demographic demographic, Boundary boundary) {
        this.voteData = voteData;
        this.demographic = demographic;
        this.boundary = boundary;
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
