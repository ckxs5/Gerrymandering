package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Enum.PreferenceType;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;

import java.util.*;

import static com.example.gerrymanderdemo.model.Enum.Party.DEMOCRATIC;
import static com.example.gerrymanderdemo.model.Enum.Party.REPUBLICAN;

public class Algorithm {
    private Map<String, String> preference;
    private State state;
    private ClusterManager clusterManager;
    float tempObjectiveFunctionValue;
    private District currentDistrict = null;
    private HashMap<District, Double> currentScores;//TODO: Map district to its score
    private HashMap<Long, Long> redistrictingPlan;//TODO: Map key precinctId to its district's id


    public Algorithm(Map<String, String> preference, State state) {
        this.preference = preference;
        this.state = state;
        this.clusterManager = new ClusterManager(
                RaceType.valueOf(preference.get(PreferenceType.COMMUNITY_OF_INTEREST.toString())),
                Integer.parseInt(preference.get(PreferenceType.NUM_DISTRICTS.toString())),
                new ArrayList<>(PrecinctManager.getPrecincts(StateName.MINNESOTA).values())
                );
    }

    public void setRedistrictingPlan(){
        for (District d: state.getDistricts()){
            for (Precinct p: d.getPrecincts()){
                redistrictingPlan.put(p.getId(), d.getId());
            }
        }
    }

    public State graphPartition() {
        clusterManager.run();
        state.setDistricts(clusterManager.toDistricts());
        return state;
    }

    public State graphPartitionOnce() {
        clusterManager.runOnce();
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

    public Map<String, String> getPreference() {
        return preference;
    }

    public void setPreference(Map<String, String> preference) {
        this.preference = preference;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ClusterManager getClusterManager() {
        return clusterManager;
    }

    public void setClusterManager(ClusterManager clusterManager) {
        this.clusterManager = clusterManager;
    }

    public float getTempObjectiveFunctionValue() {
        return tempObjectiveFunctionValue;
    }

    public void setTempObjectiveFunctionValue(float tempObjectiveFunctionValue) {
        this.tempObjectiveFunctionValue = tempObjectiveFunctionValue;
    }

    public District getCurrentDistrict() {
        return currentDistrict;
    }

    public void setCurrentDistrict(District currentDistrict) {
        this.currentDistrict = currentDistrict;
    }

    public HashMap<District, Double> getCurrentScores() {
        return currentScores;
    }

    public void setCurrentScores(HashMap<District, Double> currentScores) {
        this.currentScores = currentScores;
    }

    public HashMap<Long, Long> getRedistrictingPlan() {
        return redistrictingPlan;
    }

    public void setRedistrictingPlan(HashMap<Long, Long> redistrictingPlan) {
        this.redistrictingPlan = redistrictingPlan;
    }

    //TODO:check contiguity for moving precinct p out of district d
    //returns true if contiguous
    private boolean checkContiguity(Precinct p, District d){
        Set<String> neighborIDs = p.getNeighborIDs();
        HashSet<Precinct> neededPrecincts = new HashSet<Precinct>();
        HashSet<Precinct> neighborToExplore = new HashSet<Precinct>();//potential sources of exploration
        HashSet<Precinct> exploreNeighbors = new HashSet<Precinct>();// neighbors already explored
        exploreNeighbors.add(p);// add the precinct being moved, to ensure it won't be used
        // if a neighbor is in the district that's losing a precinct, we need to make sure they're still contiguous
        for (String s : neighborIDs) {
            // if neighbor is in the district we're losing from
            if ((redistrictingPlan.get(s)).equals(d.getId())) {
                Precinct n = d.getPrecinct(s);
                neededPrecincts.add(n);
            }
        }
        // if there are no same - district neighbors for the node, returns false
        if(neededPrecincts.size() == 0){
            return false;
        }
        // add an arbitrary same - district neighbor to the sources of exploration
        neighborToExplore.add(neededPrecincts.iterator().next());
        // while we still need iDs and still have neighbors to explore
        while (neighborToExplore.size() != 0){
            // take an arbitrary precinct from the sources of exploration
            Precinct n = neighborToExplore.iterator().next();
            for (String s :n.getNeighborIDs()) {
                //we only care about neighbors in our district,d
                if (redistrictingPlan.get(s).equals(d.getId())) {
                    Precinct nn = d.getPrecinct(s);
                    // if we've hit one of our needed precincts, check it off
                    if (neededPrecincts.contains(nn)) {
                        neededPrecincts.remove(nn);
                        if (neededPrecincts.size() == 0){
                            return true;
                        }
                    }
                    // add any neighbors in same district to neighborsToExplore if not in exploredNeighbors
                    if (!exploreNeighbors.contains(nn)){
                        neighborToExplore.add(nn);
                    }
                }
            }
            // check this precinct off
            exploreNeighbors.add(n);
            neighborToExplore.remove(n);
        }
        return (neededPrecincts.size() == 0);
        //return false;
    }
    //TODO
    public double rateDistrict(District d) {
        return 0;
    }

    /*
    Partisan fairness:
    100% - underrepresented party's winning margin
    OR
    underrepresented party's losing margin
    (we want our underrepresented party to either win by a little or lose by a lot - fewer wasted votes)
*/
    public double ratePartisanFairness(District d){
        // temporary section
        int totalVote = 0;
        int totalREPvote = 0;
        int totalDistricts = 0;
        int totalREPDistricts = 0;
        for (District sd: state.getDistricts()){
            totalVote += sd.getData().getVoteData().getVote(REPUBLICAN);
            totalVote += sd.getData().getVoteData().getVote(DEMOCRATIC);
            totalREPvote += sd.getData().getVoteData().getVote(REPUBLICAN);
            totalDistricts += 1;
            if (sd.getData().getVoteData().getVote(REPUBLICAN)>sd.getData().getVoteData().getVote(DEMOCRATIC)){
                totalREPDistricts += 1;
            }
        }
        int idealDistrictChange = ((int)Math.round(totalDistricts *((1.0*totalREPvote)/totalVote))) - totalREPDistricts;
        // end temporary section
        if (idealDistrictChange == 0){
            return 1.0;
        }
        int gv = d.getData().getVoteData().getVote(REPUBLICAN);
        int dv = d.getData().getVoteData().getVote(DEMOCRATIC);
        int tv = gv+dv;
        int margin = gv-dv;
        if(tv == 0){
            return 1.0;
        }
        int win_v = Math.max(gv,dv);
        int loss_v = Math.min(gv,dv);
        int inefficient_V;
        if(idealDistrictChange * margin > 0){
            inefficient_V = win_v - loss_v;
        }else{
            inefficient_V = loss_v;
        }
        return 1.0 - ((inefficient_V * 1.0)/tv);
    }

    /*
    wasted votes:
    statewide: abs(winning party margin - losing party votes)
*/
    public double rateStatewideEfficiencyGap(District d) {
        int iv_g = 0;
        int iv_d = 0;
        int tv = 0;
        for (District sd : state.getDistricts()) {
            int gv = sd.getData().getVoteData().getVote(REPUBLICAN);
            int dv = sd.getData().getVoteData().getVote(DEMOCRATIC);
            if (gv > dv) {
                iv_d =+ dv;
                iv_g += (gv + dv);
            } else if (dv > gv ) {
                iv_g += gv;
                iv_d += (dv - gv);
            }
            tv += gv;
            tv += dv;
        }
        return 1.0 - ((Math.abs(iv_g - iv_d) * 1.0) / tv);
    }
    /*
        wasted votes:
        abs(winning party margin - losing party votes)
    */
    public double rateEfficiencyGap(District d) {
        int gv = d.getData().getVoteData().getVote(REPUBLICAN);
        int dv = d.getData().getVoteData().getVote(DEMOCRATIC);
        int tv = gv + dv;
        if (tv == 0){
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V = Math.abs(loss_v - (win_v - loss_v));
        return 1.0 - ((inefficient_V * 1.0) / tv);
    }
    /*
        COMPETITIVENESS:
        1.0 - margin of victory
    */
    public double rateCOMPETITIVENESS(District d) {
        int gv = d.getData().getVoteData().getVote(REPUBLICAN);
        int dv = d.getData().getVoteData().getVote(DEMOCRATIC);
        return 1.0 - (Math.abs(gv - dv) / (gv + dv));
    }

    public double rateGERRYMANDER_REPUBLICAN(District d) {
        int gv = d.getData().getVoteData().getVote(REPUBLICAN);
        int dv = d.getData().getVoteData().getVote(DEMOCRATIC);
        int tv = gv + dv;
        int margin = gv - dv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V;
        if (margin > 0) {
            inefficient_V = win_v - loss_v;
        }
        else {
            inefficient_V = loss_v;
        }
        return 1.0 - ((inefficient_V * 1.0) / tv);
        }

    public double rateGERRYMANDER_DEMOCRATIC(District d) {
        int gv = d.getData().getVoteData().getVote(REPUBLICAN);
        int dv = d.getData().getVoteData().getVote(DEMOCRATIC);
        int tv = gv + dv;
        int margin = dv - gv;
        if (tv == 0) {
            return 1.0;
        }
        int win_v = Math.max(gv, dv);
        int loss_v = Math.min(gv, dv);
        int inefficient_V;
        if (margin > 0) {
            inefficient_V = win_v - loss_v;
        }
        else {
            inefficient_V = loss_v;
        }
        return 1.0 - ((inefficient_V * 1.0) / tv);
    }

    public double rateCompactnessLenWid(District d){
        double length = d.getLength();
        double width = d.getWidth();
        return length/width;
    }

    public double rateCompactnessBorder(District d){
        double borderCount = d.getBorderPrecincts().size();
        double total = d.getPrecincts().size();
        return 1-(borderCount/total);
    }


}
