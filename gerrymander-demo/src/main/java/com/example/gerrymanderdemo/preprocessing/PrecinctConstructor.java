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
    @JsonProperty("county")
    private String county;

    @JsonProperty("OtherParties")
    private int otherParties;

    @JsonProperty("Democrat")
    private int democratic;

    @JsonProperty("Republican")
    private int republican;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("TotalVote")
    private int totalVote;

    @JsonProperty("TotalRatio")
    private int totalRatio;

    @JsonProperty("Total")
    private int total;

    @JsonProperty("OneRace")
    private int oneRace;

    @JsonProperty("TwoOrMoreRaces")
    private int twoOrMoreRaces;

    @JsonProperty("White")
    private int caucasian;

    @JsonProperty("Black")
    private int africanAmerican;

    @JsonProperty("NativeAmerican")
    private int nativeAmerican;

    @JsonProperty("Asian")
    private int asian;

    @JsonProperty("PacificIslander")
    private int pacificIslander;

    @JsonProperty("Others")
    private int others;

    @JsonProperty("Hispanic")
    private int hispanic;

    @JsonProperty("boundary")
    private double[][] boundary;

    public PrecinctConstructor() {
    }

    public String getCounty() { return county; }

    public void setCounty(String county) { this.county = county; }

    public int getOtherParties() { return otherParties; }

    public void setOtherParties(int otherParties) { this.otherParties = otherParties; }

    public int getDemocratic() { return democratic; }

    public void setDemocratic(int democratic) { this.democratic = democratic; }

    public int getRepublican() { return republican; }

    public void setRepublican(int republican) { this.republican = republican; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public int getTotalVote() { return totalVote; }

    public void setTotalVote(int totalVote) { this.totalVote = totalVote; }

    public int getTotalRatio() { return totalRatio; }

    public void setTotalRatio(int totalRatio) { this.totalRatio = totalRatio; }

    public int getTotal() { return total; }

    public void setTotal(int total) { this.total = total; }

    public int getOneRace() { return oneRace; }

    public void setOneRace(int oneRace) { this.oneRace = oneRace; }

    public int getTwoOrMoreRaces() { return twoOrMoreRaces; }

    public void setTwoOrMoreRaces(int twoOrMoreRaces) { this.twoOrMoreRaces = twoOrMoreRaces; }

    public int getCaucasian() { return caucasian; }

    public void setCaucasian(int caucasian) { this.caucasian = caucasian; }

    public int getAfricanAmerican() { return africanAmerican; }

    public void setAfricanAmerican(int africanAmerican) { this.africanAmerican = africanAmerican; }

    public int getNativeAmerican() { return nativeAmerican; }

    public void setNativeAmerican(int nativeAmerican) { this.nativeAmerican = nativeAmerican; }

    public int getAsian() { return asian; }

    public void setAsian(int asian) { this.asian = asian; }

    public int getPacificIslander() { return pacificIslander; }

    public void setPacificIslander(int pacificIslander) { this.pacificIslander = pacificIslander; }

    public int getOthers() { return others; }

    public void setOthers(int others) { this.others = others; }

    public int getHispanic() { return hispanic; }

    public void setHispanic(int hispanic) { this.hispanic = hispanic; }

    public double[][] getBoundary() { return boundary; }

    public void setBoundary(double[][] boundary) { this.boundary = boundary; }

    public Precinct toIdPrecinct(){
        Precinct precinct = new Precinct();
        precinct.setId(id);
        precinct.setCounty(county);
        precinct.setName(name);
        precinct.setState(StateName.MINNESOTA);
        return precinct;
    }

    public Vote toVote() {
        Vote vote = new Vote();
        vote.setVote(Party.DEMOCRATIC, democratic);
        vote.setVote(Party.REPUBLICAN, republican);
        vote.setVote(Party.OTHERS, otherParties);
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
        demographic.setPopulation(RaceType.CAUCASIAN, caucasian);
        demographic.setPopulation(RaceType.AFRICAN_AMERICAN, africanAmerican);
        demographic.setPopulation(RaceType.ASIAN_PACIFIC_AMERICAN, asian);
        demographic.setPopulation(RaceType.NATIVE_AMERICAN, nativeAmerican);
        demographic.setPopulation(RaceType.OTHERS, others);
        demographic.setPopulation(RaceType.HISPANIC_LATINO_AMERICAN, hispanic);
        return demographic;
    }

    @Override
    public String toString() {
        return "PrecinctConstructor{" +
                "county='" + county + '\'' +
                ",\n otherParties=" + otherParties +
                ",\n democratic=" + democratic +
                ",\n republican=" + republican +
                ",\n name='" + name + '\'' +
                ",\n id=" + id +
                ",\n type='" + type + '\'' +
                ",\n totalVote=" + totalVote +
                ",\n totalRatio=" + totalRatio +
                ",\n total=" + total +
                ",\n oneRace=" + oneRace +
                ",\n twoOrMoreRaces=" + twoOrMoreRaces +
                ",\n caucasian=" + caucasian +
                ",\n africanAmerican=" + africanAmerican +
                ",\n nativeAmerican=" + nativeAmerican +
                ",\n asian=" + asian +
                ",\n pacificIslander=" + pacificIslander +
                ",\n others=" + others +
                ",\n hispanic=" + hispanic +
                ",\n boundary=" + Arrays.toString(boundary) +
                '}';
    }
}
