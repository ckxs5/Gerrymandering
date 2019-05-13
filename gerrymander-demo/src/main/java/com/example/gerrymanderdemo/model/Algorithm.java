package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Enum.PreferenceType;
import com.example.gerrymanderdemo.model.Enum.RaceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Algorithm {
    private Map<String, String> preference;
    private State state;
    private ClusterManager clusterManager;
    float tempObjectiveFunctionValue;
    private District currentDistrict = null;
    private HashMap<District, Double> currentScores;
    private HashMap<String, String> redistrictingPlan;//TODO: Map key precinctId to its district's id


    public Algorithm(Map<String, String> preference, State state, PrecinctService service) {
        this.preference = preference;
        this.state = state;
        this.clusterManager = new ClusterManager(service,
                RaceType.valueOf(preference.get(preference.get(PreferenceType.NUM_DISTRICTS.toString()))),
                Integer.parseInt(preference.get(PreferenceType.NUM_DISTRICTS.toString())));
    }

    public State graphPartisian() {
        clusterManager.run();
        state.setDistricts(clusterManager.toDistricts());
        return state;
    }
    //TODO
    public State runAlgorithm(){return null;}
    //TODO
    public float getOF(){return tempObjectiveFunctionValue;}



    public Move makeMove(){
        if (currentDistrict == null){
            currentDistrict = getWorstDistrict();
        }
        District startDistrict = currentDistrict;
        Move m = getMoveFromDistrict(startDistrict);
        if (m == null){
            return makeMove_secondary();
        }
        return m;
    }

    //This is similar to makeMove, but reverse to put neighbor precinct to startDistrict and check
    public Move makeMove_secondary() {
        List<District> districts = getWorstDistricts();//TODO:getWorstDistricts()
        districts.remove(0);
        while (districts.size() > 0) {
            District startDistrict = districts.get(0);
            Move m = getMoveFromDistrict(startDistrict);
            if (m != null) {
                return m;
            }
            districts.remove(0);
        }
        return null;
    }

    // TODO: returns a list of districts sorted from worst to best
    public List<District> getWorstDistricts() {
        return null;
    }

    public District getWorstDistrict() {
        District worstDistrict = null;
        double minScore = Double.POSITIVE_INFINITY;
        for (District d : state.getDistricts()) {//TODO: getDistricts()
            double score = currentScores.get(d);
            if (score < minScore) {
                worstDistrict = d;
                minScore = score;
            }
        }
        return worstDistrict;
    }

    public Move getMoveFromDistrict(District startDistrict){
        Set<Precinct> precincts = startDistrict.getBorderPrecincts();//TODO: getBorderPrecincts
        for (Precinct p:precincts){
            Set<String> neighborIDs = p.getNeighborIDs();
            for (String n: neighborIDs){
                if(startDistrict.getPrecinct(n) == null){//take the precinct that is not in startDistrict
                    District neighborDistrict = state.getDistrict(redistrictingPlan.get(n));//TODO: getDistrict()
                    Move move = testMove(neighborDistrict,startDistrict,p);
                    if (move != null){
                        System.out.println("Moving p to neighborDistrict(neighborID = "+n+")");
                        currentDistrict = startDistrict;
                        return move;
                    }
                    move = testMove(startDistrict,neighborDistrict,neighborDistrict.getPrecinct(n));
                    if (move != null){
                        System.out.println("Move n to Start district: "+startDistrict.getId());
                        currentDistrict = startDistrict;
                        return move;
                    }
                }
            }
        }
        return null;
    }

    private Move testMove(District to, District from, Precinct p) {
        if (!checkContiguity(p,from)) {//TODO:checkContiguity()
            return null;
        }
        Move m = new Move(to, from, p);
        double initial_score = currentScores.get(to) + currentScores.get(from);
        m.execute();
        double to_score = rateDistrict(to);//TODO:rateDistrict()
        double from_score = rateDistrict(from);
        double final_score = (to_score + from_score);
        double change = final_score - initial_score;
        if (change <= 0) {
            m.undo();
            return null;
        }
        currentScores.put(to, to_score);
        currentScores.put(from, from_score);
        redistrictingPlan.put(p.getId(), to.getId());
        return m;
    }

    //TODO:check contiguity for moving precinct p out of district d
    //returns true if contiguous
    private boolean checkContiguity(Precinct p, District d){
        return true;
    }
    //TODO
    public double rateDistrict(District d) {
        return 0;
    }

}
