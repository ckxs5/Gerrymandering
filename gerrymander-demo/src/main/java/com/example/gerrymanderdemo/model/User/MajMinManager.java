package com.example.gerrymanderdemo.model.User;

import com.example.gerrymanderdemo.model.District;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Move;
import com.example.gerrymanderdemo.model.Precinct;

import java.util.Collection;
import java.util.Set;

public class MajMinManager {
    private Collection<District> districts;
    private RaceType minorityRace;
    private double downRatio;
    private double upRatio;
    private Collection<District> mmDistricts;
    private Collection<District> candidateDistricts;
    private District bestCandidate;
    private boolean addOrRemove;

    public MajMinManager(Collection<District> districts,RaceType minorityRace,double downRatio,double upRatio,
                         Collection<District> mmDistricts,Collection<District> candidateDistricts,District bestCandidate,boolean addOrRemove){
        this.districts = districts;
        this.minorityRace = minorityRace;
        this.downRatio = downRatio;
        this.upRatio = upRatio;
        this.mmDistricts = mmDistricts;
        this.candidateDistricts = candidateDistricts;
        this.bestCandidate = bestCandidate;
        this.addOrRemove = addOrRemove;
    }


    public void setBestCandidateDistrict(){
        if(addOrRemove==true){//we want add a mmDistrict
            double bestPercent = 0;
            for(District d: candidateDistricts) {
                double dPercent = d.getData().getDemographic().getPercentByRace(minorityRace);
                bestPercent = dPercent>bestPercent?dPercent:bestPercent;
                bestCandidate = d;
            }
        }else{//we want remove a mmDistrict
            double bestPercent = 1;
            for(District d: mmDistricts) {
                double dPercent = d.getData().getDemographic().getPercentByRace(minorityRace);
                bestPercent = dPercent<bestPercent?dPercent:bestPercent;
                bestCandidate = d;
            }
        }
    }



    //add mmDistrict-true, remove mmDistrict-false
    public Move getMoveFromDistrict(){
        Long districtId = bestCandidate.getId();
        Set<Precinct> borderPrecincts = bestCandidate.getBorderPrecincts();//TODO: getBorderPrecincts
        for (Precinct p:borderPrecincts){
            for (Precinct nn: p.getNeighbors()){
                if(nn.getDistrictId()!=districtId){//take the precinct that is not in startDistrict
                    District neighborDistrict = findNeighborDistrict(nn.getDistrictId());
                    Move move = null;
                    move = addOrRemove?testAddMove(neighborDistrict, bestCandidate, p):testRemoveMove(neighborDistrict, bestCandidate, p);
                    if (move != null){
                        System.out.println("Moving p to neighborDistrict(neighborID = "+nn.getDistrictId()+")");
                        return move;
                    }
                    move = addOrRemove?testAddMove(bestCandidate,neighborDistrict,nn):testRemoveMove(bestCandidate,neighborDistrict,nn);
                    if (move != null){
                        System.out.println("Move n to Start district: "+bestCandidate.getId());
                        return move;
                    }
                }
            }
        }
        return null;
    }

    public District findNeighborDistrict(Long id){
        for(District d: districts){
            if (d.getId() == id){
                return d;
            }
        }
        return null;
    }

    //we want add a mmDistrict
    public Move testAddMove(District to, District from, Precinct p) {
        Move m = new Move(to, from, p);
        double initialPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        m.execute();
        double finalPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        if(finalPercent<=initialPercent){
            m.undo();
            return null;
        }
        return m;
    }

    //we want remove a mmDistrict
    public Move testRemoveMove(District to, District from, Precinct p) {
        Move m = new Move(to, from, p);
        double initialPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        m.execute();
        double finalPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        if(finalPercent>=initialPercent){
            m.undo();
            return null;
        }
        return m;
    }

}
