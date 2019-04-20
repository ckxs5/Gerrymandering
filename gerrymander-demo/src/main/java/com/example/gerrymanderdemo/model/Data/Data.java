package com.example.gerrymanderdemo.model.Data;

public class Data {
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
}
