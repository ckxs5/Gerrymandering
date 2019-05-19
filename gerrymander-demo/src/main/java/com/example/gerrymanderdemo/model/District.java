package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.Order;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import org.assertj.core.util.Sets;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Data data;
    @Transient
    private Set<Precinct> precincts;
    @Transient
    private Set<Precinct> borderPrecincts;

    public District(){
    }

    public District(District dist){
        this.data = new Data(dist.getData());
        this.precincts = new HashSet<>();
        this.precincts.addAll(dist.getPrecincts());
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Set<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(Set<Precinct> precincts) {
        this.precincts = precincts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Precinct> getNeighbors(){
        Set<Precinct> neighbours = new HashSet<>();
        for (Precinct p: precincts) {
            neighbours.addAll(p.getNeighbors());
        }
        return neighbours;
    }

    public Precinct findBestCandidate(RaceType type, Order order){
        Precinct minCandidate = (Precinct) precincts.toArray()[0];
        Precinct maxCandidate = (Precinct) precincts.toArray()[0];

        for (Precinct p : precincts) {
            if (minCandidate.getData().getDemographic().getPercentByRace(type)
                    > p.getData().getDemographic().getPercentByRace(type)) {
                minCandidate = p;
            }
            if (maxCandidate.getData().getDemographic().getPercentByRace(type)
                    < p.getData().getDemographic().getPercentByRace(type)) {
                maxCandidate = p;
            }
        }

        switch (order) {
            case ASCENDING:
                return minCandidate;
            case DESCENDING:
                return maxCandidate;
            default:
                return null;
        }
    }

    public District testPrecinct(Precinct precinct){
        //TODO: This can only work when deep copy is implemented correctly
        District result = new District(this);
        result.addPrecinct(precinct);
        return result;
    }

    public void addPrecinct(Precinct precinct){
        if (precincts.add(precinct)) {
            precinct.setDistrictId(this.id);
            this.data.add(precinct.getData());
        }
    }

    public Precinct removePrecinct(Precinct precinct){
        if (precincts.remove(precinct)) {
            this.data.remove(precinct.getData());
            return precinct;
        }
        return null;
    }

    public double compareMinorityRatio(RaceType communityOfInterest, Range<Double> tRatio){
        double actual = this.getData().getDemographic().getPercentByRace(communityOfInterest);
        if (tRatio.getUpperBound() < actual) {
            return actual - tRatio.getUpperBound();
        } else if (tRatio.getLowerBound() > actual) {
            return tRatio.getLowerBound() - actual;
        } else {
            return 0;
        }
    }

    public boolean isMajorityMinority(RaceType communityOfInterest, Range range){
        return range.isIncluding(this.getData().getDemographic().getPercentByRace(communityOfInterest));
    }

    public double getGerrymanderingScore(){
        return 1.0 * Math.abs(this.data.getVoteData().getVote(Party.DEMOCRATIC)
                - this.data.getVoteData().getVote(Party.REPUBLICAN))
                / Math.max(this.data.getVoteData().getVote(Party.DEMOCRATIC),
                this.data.getVoteData().getVote(Party.REPUBLICAN));
    }


    //TODO: we should change precincts type to a HashMap<precinctId, precinct>
    public Precinct getPrecinct(Long id) {
        for (Precinct p : precincts) {
            if(p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public double getLength(){
        double[][] geoJson= data.getBoundary().getGeoJSON();
        double min_lon = geoJson[0][0];
        double max_lon = geoJson[0][0];
        for (int i = 0; i < geoJson.length; i++) {
            if(geoJson[i][0] < min_lon){
                min_lon = geoJson[i][0];
            }else{
                if(geoJson[i][0] > max_lon){
                    max_lon = geoJson[i][0];
                }
            }
        }
        return Math.abs(max_lon - min_lon);
    }

    public double getWidth(){
        double[][] geoJson= data.getBoundary().getGeoJSON();
        double min_lat = geoJson[0][1];
        double max_lat = geoJson[0][1];
        for (int i = 0; i < geoJson.length; i++) {
            if(geoJson[i][1] < min_lat){
                min_lat = geoJson[i][1];
            }else{
                if(geoJson[i][1] > max_lat){
                    max_lat = geoJson[i][1];
                }
            }
        }
        return Math.abs(max_lat - min_lat );
    }

    public Set<Precinct> getBorderPrecincts(){
        for(Precinct p: precincts){
            for(Precinct np: p.getNeighbors()){
                if(!np.getDistrictId().equals(id)){
                    borderPrecincts.add(p);
                }
            }
        }
        return borderPrecincts;
    }





}
