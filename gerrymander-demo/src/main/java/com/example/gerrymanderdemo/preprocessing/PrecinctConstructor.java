package com.example.gerrymanderdemo.preprocessing;

import com.example.gerrymanderdemo.model.Data.Boundary;
import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.model.Precinct;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class PrecinctConstructor {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("country")
    private String county;
    @JsonProperty("state")
    private StateName state;
    @JsonProperty("Democratic-Farmer-Labor")
    private int democratic;
    @JsonProperty("american delta")
    private int delta;
    @JsonProperty("libertarian")
    private int libertarian;
    @JsonProperty("green")
    private int green;
    @JsonProperty("republican")
    private int republican;
    @JsonProperty("Independence")
    private int independence;
    @JsonProperty("Socialist Workers")
    private int socialistWorkers;
    @JsonProperty("Legal Marijuana Now")
    private int lewgalMarijuanaNow;
    @JsonProperty("White")
    private int caucassion;
    @JsonProperty("Black or African American")
    private int africanAmerican;
    @JsonProperty("American Indian and Alaska Native")
    private int nativeAmerican;
    @JsonProperty("Asian")
    private int asian;
    @JsonProperty("Hispanic or Latino (of any race)")
    private int hispanic;
    @JsonProperty("Total Population")
    private int total;
    @JsonProperty("type")
    private String type;
    @JsonProperty("boundary")
    private double[][] boundary;
    @JsonProperty("neighbors")
    private Long[] neighbors;

    public PrecinctConstructor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getDemocratic() {
        return democratic;
    }

    public void setDemocratic(int democratic) {
        this.democratic = democratic;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getLibertarian() {
        return libertarian;
    }

    public void setLibertarian(int libertarian) {
        this.libertarian = libertarian;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getRepublican() {
        return republican;
    }

    public void setRepublican(int republican) {
        this.republican = republican;
    }

    public int getIndependence() {
        return independence;
    }

    public void setIndependence(int independence) {
        this.independence = independence;
    }

    public int getSocialistWorkers() {
        return socialistWorkers;
    }

    public void setSocialistWorkers(int socialistWorkers) {
        this.socialistWorkers = socialistWorkers;
    }

    public int getLewgalMarijuanaNow() {
        return lewgalMarijuanaNow;
    }

    public void setLewgalMarijuanaNow(int lewgalMarijuanaNow) {
        this.lewgalMarijuanaNow = lewgalMarijuanaNow;
    }

    public int getCaucassion() {
        return caucassion;
    }

    public void setCaucassion(int caucassion) {
        this.caucassion = caucassion;
    }

    public int getAfricanAmerican() {
        return africanAmerican;
    }

    public void setAfricanAmerican(int africanAmerican) {
        this.africanAmerican = africanAmerican;
    }

    public int getNativeAmerican() {
        return nativeAmerican;
    }

    public void setNativeAmerican(int nativeAmerican) {
        this.nativeAmerican = nativeAmerican;
    }

    public int getAsian() {
        return asian;
    }

    public void setAsian(int asian) {
        this.asian = asian;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[][] getBoundary() {
        return boundary;
    }

    public void setBoundary(double[][] boundary) {
        this.boundary = boundary;
    }

    public Long[] getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(Long[] neighbors) {
        this.neighbors = neighbors;
    }

    public int getHispanic() {
        return hispanic;
    }

    public void setHispanic(int hispanic) {
        this.hispanic = hispanic;
    }

    public Precinct toIdPrecinct(){
        Precinct precinct = new Precinct();
        precinct.setId(id);
        precinct.setCounty(county);
        precinct.setName(name);
        precinct.setState(state);
        return precinct;
    }

    public Vote toVote() {
        Vote vote = new Vote();
        vote.setVote(Party.DEMOCRATIC, democratic);
        vote.setVote(Party.REPUBLICAN, republican);
        vote.setVote(Party.OTHERS,
                this.delta + this.green + this.independence + this.lewgalMarijuanaNow +
                        this.libertarian + this.socialistWorkers);
        return vote;
    }

    public Boundary toBoundary(){
        Boundary boundary = new Boundary();
        boundary.setGeoJSON(this.boundary);
        return boundary;
    }

    public Demographic toDemographic(){
        Demographic demographic = new Demographic();
        demographic.setPopulation(RaceType.ALL, total);
        demographic.setPopulation(RaceType.CAUCASIAN, caucassion);
        demographic.setPopulation(RaceType.AFRICAN_AMERICAN, africanAmerican);
        demographic.setPopulation(RaceType.ASIAN_PACIFIC_AMERICAN, asian);
        demographic.setPopulation(RaceType.NATIVE_AMERICAN, nativeAmerican);
        demographic.setPopulation(RaceType.OTHERS, total - caucassion - africanAmerican - asian - nativeAmerican);
        demographic.setPopulation(RaceType.HISPANIC_LATINO_AMERICAN, hispanic);
        return demographic;
    }


    @Override
    public String toString() {
        return "PrecinctConstructor{" +
                "id=" + id +
                ",\n name='" + name + '\'' +
                ",\n county='" + county + '\'' +
                ",\n democratic=" + democratic +
                ",\n delta=" + delta +
                ",\n libertarian=" + libertarian +
                ",\n green=" + green +
                ",\n republican=" + republican +
                ",\n independence=" + independence +
                ",\n socialistWorkers=" + socialistWorkers +
                ",\n lewgalMarijuanaNow=" + lewgalMarijuanaNow +
                ",\n caucassion=" + caucassion +
                ",\n africanAmerican=" + africanAmerican +
                ",\n nativeAmerican=" + nativeAmerican +
                ",\n asian=" + asian +
                ",\n hispanic=" + hispanic +
                ",\n total=" + total +
                ",\n type='" + type + '\'' +
                ",\n boundary=" + Arrays.toString(boundary) +
                ",\n neighbors=" + Arrays.toString(neighbors) +
                "\n}";
    }
}
