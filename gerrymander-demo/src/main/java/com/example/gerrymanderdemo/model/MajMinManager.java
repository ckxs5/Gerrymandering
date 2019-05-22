package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.RaceType;


import java.util.*;
import java.util.stream.Collectors;

public class MajMinManager {
    private Collection<District> districts;
    private RaceType minorityRace;
    private Range<Double> range;
    private List<District> mmDistricts;
    private List<District> candidateDistricts;
    private District bestCandidate;
    private boolean addOrRemove;


    public MajMinManager(State state, RaceType minorityRace, double downRatio, double upRatio, boolean addOrRemove){
        this.districts = state.getDistricts();
        this.minorityRace = minorityRace;
        range = new Range<>(downRatio, upRatio);
        this.mmDistricts = districts.stream().filter(district ->
                range.isIncluding(district.getData().getDemographic().getPercentByRace(minorityRace))
                ).collect(Collectors.toList());
        this.candidateDistricts = new ArrayList<>(districts);
        candidateDistricts.removeAll(mmDistricts);

        for (District d : mmDistricts) {
            System.out.printf("MM : %s, %f\n", d.getId(), d.getData().getDemographic().getPercentByRace(minorityRace));
        }
        for (District d : candidateDistricts) {
            System.out.printf("CD : %s, %f\n", d.getId(), d.getData().getDemographic().getPercentByRace(minorityRace));
        }

        this.addOrRemove = addOrRemove;
        setBestCandidateDistrict();
    }


    private void setBestCandidateDistrict(){
        System.out.println("setBestCandidateDistrict");
        if(addOrRemove){//we want add a mmDistrict
            double bestPercent = 0;
            for(District d: candidateDistricts) {
                double dPercent = d.getData().getDemographic().getPercentByRace(minorityRace);
                System.out.printf("candidate: %s, %f \n", d.getId(), dPercent);
                if(dPercent > bestPercent){
                    bestPercent = dPercent;
                    bestCandidate = d;
                }
            }
        }else{//we want remove a mmDistrict
            double bestPercent = 1;
            for(District d: mmDistricts) {
                double dPercent = d.getData().getDemographic().getPercentByRace(minorityRace);
                System.out.printf("mmDistrict: %s, %f \n", d.getId(), dPercent);
                if(dPercent < bestPercent){
                    bestPercent = dPercent;
                    bestCandidate = d;
                }
            }
        }
        System.out.println("The best Candidate: "+ bestCandidate);
    }

    //add mmDistrict-true, remove mmDistrict-false
    public Move moveFromDistrict(){
        District district = bestCandidate;
        List<Precinct> borderPrecincts = bestCandidate.getBorderPrecincts();//TODO: getBorderPrecincts
        for (Precinct p : borderPrecincts){
            for (Precinct nn: p.getNeighbors()){
                if(!nn.getDistrict().equals(district)){//take the precinct that is not in startDistrict
                    District neighborDistrict = findNeighborDistrict(nn.getDistrict());
                    Move move = addOrRemove ? addMM_testMove(bestCandidate, neighborDistrict, p) : removeMM_testMove(bestCandidate, neighborDistrict, p);
                    if (move != null){
                        System.out.println("Moving p to neighborDistrict(neighborID = "+nn.getDistrict()+")");
                        return move;
                    }
                    move = addOrRemove ? addMM_testMove(neighborDistrict, bestCandidate, nn) : removeMM_testMove(neighborDistrict, bestCandidate, nn);
                    if (move != null){
                        System.out.println("Move n to Start district: " + bestCandidate.getId());
                        return move;
                    }
                }
            }
        }
        System.out.println("No Move Found");
        return null;
    }

    public District findNeighborDistrict(District id){
        for(District d: districts){
            if (d.getId().equals(id)){
                return d;
            }
        }
        return null;
    }

    //we want add a mmDistrict
    public Move addMM_testMove(District to, District from, Precinct p) {
        Move m = new Move(to, from, p);
        double initialPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        m.execute();
        double finalPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        if(finalPercent <= initialPercent || finalPercent > range.getUpperBound()){
            m.undo();
            return null;
        }
        return m;
    }

    //we want remove a mmDistrict
    public Move removeMM_testMove(District to, District from, Precinct p) {
        Move m = new Move(to, from, p);
        double initialPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        m.execute();
        double finalPercent = bestCandidate.getData().getDemographic().getPercentByRace(minorityRace);
        if(finalPercent >= initialPercent){
            m.undo();
            return null;
        }
        return m;
    }

    public Collection<District> getDistricts() {
        return districts;
    }

    public void setDistricts(Collection<District> districts) {
        this.districts = districts;
    }

    public RaceType getMinorityRace() {
        return minorityRace;
    }

    public void setMinorityRace(RaceType minorityRace) {
        this.minorityRace = minorityRace;
    }

    public Range<Double> getRange() {
        return range;
    }

    public void setRange(Range<Double> range) {
        this.range = range;
    }

    public List<District> getMmDistricts() {
        return mmDistricts;
    }

    public void setMmDistricts(List<District> mmDistricts) {
        this.mmDistricts = mmDistricts;
    }

    public List<District> getCandidateDistricts() {
        return candidateDistricts;
    }

    public void setCandidateDistricts(List<District> candidateDistricts) {
        this.candidateDistricts = candidateDistricts;
    }

    public District getBestCandidate() {
        return bestCandidate;
    }

    public void setBestCandidate(District bestCandidate) {
        this.bestCandidate = bestCandidate;
    }

    public boolean isAddOrRemove() {
        return addOrRemove;
    }

    public void setAddOrRemove(boolean addOrRemove) {
        this.addOrRemove = addOrRemove;
    }
}
