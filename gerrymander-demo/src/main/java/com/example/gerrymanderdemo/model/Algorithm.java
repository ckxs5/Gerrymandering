package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.PreferenceType;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.model.Exception.NotAnotherMoveException;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

import static com.example.gerrymanderdemo.model.Enum.Party.DEMOCRATIC;
import static com.example.gerrymanderdemo.model.Enum.Party.REPUBLICAN;
import static com.example.gerrymanderdemo.model.Enum.PreferenceType.*;

public class Algorithm {
    private Map<String, String> preference;
    private State state;
    private ClusterManager clusterManager;
    private float tempObjectiveFunctionValue;
    private District currentDistrict = null;
    private HashMap<District, Double> currentScores;//TODO: Map district to its score
    private HashMap<Long, Long> redistrictingPlan;//TODO: Map key precinctId to its district's id
    @Value("${population.range.variant}")
    private double populationVariant;


    public Algorithm(Map<String, String> preference, State state) {
        this.preference = preference;
        this.state = state;
        this.clusterManager = new ClusterManager(
                RaceType.valueOf(preference.get(PreferenceType.COMMUNITY_OF_INTEREST.toString())),
                Integer.parseInt(preference.get(PreferenceType.NUM_DISTRICTS.toString())),
                new ArrayList<>(PrecinctManager.getPrecincts(StateName.MINNESOTA).values())
                );
    }

    public State graphPartition() {
        clusterManager.run();
        state.setDistricts(clusterManager.toDistricts());
        return state;
    }

    public State graphPartitionOnce() throws NotAnotherMoveException{
        if (!clusterManager.runOnce()) {
            throw new NotAnotherMoveException();
        }
        state.setDistricts(clusterManager.toDistricts());
        return state;
    }

    public void setRedistrictingPlan(){
        redistrictingPlan = new HashMap<>();
        currentScores = new HashMap<>();
        for (District d: state.getDistricts()){
            for (Precinct p : d.getPrecincts()){
                redistrictingPlan.put(p.getId(), d.getId());
            }
            currentScores.put(d, rateDistrict(d));
        }

    }

    //TODO
    public State runAlgorithm(){return null;}

    public Move runTest() {
        MajMinManager majMinManager = new MajMinManager(
                getState(),
                RaceType.valueOf(preference.get("COMMUNITY_OF_INTEREST")),
                Double.parseDouble(preference.get("MAJMIN_LOW")) / 100,
                Double.parseDouble(preference.get("MAJMIN_UP")) / 100,
                true);
        District district = majMinManager.getBestCandidate();
        System.out.printf("District get from mm Manager is %s \n", district);
        Move move = majMinManager.moveFromDistrict();

        System.out.println(move.getTo());
        System.out.println(move.getFrom());
        System.out.println(move.getPrecinct());
        return move;
    }
    //TODO
    public float getOF(){return tempObjectiveFunctionValue;}


    public Move makeMove(){
        System.out.println("currentDistrict: " + currentDistrict);
        if (currentDistrict == null){
            currentDistrict = getWorstDistrict();
        }
        Move m = getMoveFromDistrict(currentDistrict);
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
        state.getDistricts().sort((o1, o2) -> {
            double rate1 = rateDistrict(o1);
            double rate2 = rateDistrict(o2);
            return Double.compare(rate1, rate2);
        });
        return state.getDistricts();
    }

    public District getWorstDistrict() {
        District worstDistrict = null;
        double minScore = Double.POSITIVE_INFINITY;
        System.out.println("minScore: " + minScore);
        System.out.println("Get Districts: " + state.getDistricts());
        for (District d : state.getDistricts()) {//TODO: getDistricts()
            double score = rateDistrict(d);
            currentScores.put(d, score);
            System.out.println("Score: " + score);
            if (score < minScore) {
                worstDistrict = d;
                minScore = score;
            }
        }
        System.out.println("Worst District: " + worstDistrict);
        return worstDistrict;
    }

    public Move getMoveFromDistrict(District startDistrict){
        List<Precinct> precincts = new ArrayList<>(startDistrict.getBorderPrecincts());
        for (Precinct p : precincts) {
            List<Long> neighborIDs = new ArrayList<>(p.getNeighborIDs());
            for (Long id : neighborIDs) {
                if (startDistrict.getPrecinct(id) == null) {//take the precinct that is not in startDistrict
                    District neighborDistrict = state.getDistrictById(redistrictingPlan.get(id));
                    Move move = testMove(neighborDistrict, startDistrict, p);
                    if (move != null) {
                        System.out.println("Moving p to neighborDistrict(neighborID = " + id + ")");
                        currentDistrict = startDistrict;
                        return move;
                    }
                    move = testMove(startDistrict, neighborDistrict, neighborDistrict.getPrecinct(id));
                    if (move != null) {
                        System.out.println("Move n to Start district: " + startDistrict.getId());
                        currentDistrict = startDistrict;
                        return move;
                    }
                }
            }
        }
        return null;
    }

    private Move testMove(District to, District from, Precinct p) {
        if (!checkContiguity(p,from)) {
            return null;
        }
        Move m = new Move(to, from, p);
        double initial_score = currentScores.get(to) + currentScores.get(from);
        m.execute();
        double to_score = rateDistrict(to);
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
        Set<Long> neighborIDs = p.getNeighborIDs();
        HashSet<Precinct> neededPrecincts = new HashSet<Precinct>();
        HashSet<Precinct> neighborToExplore = new HashSet<Precinct>();//potential sources of exploration
        HashSet<Precinct> exploreNeighbors = new HashSet<Precinct>();// neighbors already explored
        exploreNeighbors.add(p);// add the precinct being moved, to ensure it won't be used
        // if a neighbor is in the district that's losing a precinct, we need to make sure they're still contiguous
        for (Long id : neighborIDs) {
            // if neighbor is in the district we're losing from
            if ((redistrictingPlan.get(id)).equals(d.getId())) {
                Precinct n = d.getPrecinct(id);
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
            for (Long id :n.getNeighborIDs()) {
                //we only care about neighbors in our district,d
                if (redistrictingPlan.get(id).equals(d.getId())) {
                    Precinct nn = d.getPrecinct(id);
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

    public double rateDistrict(District d) {
        double objectiveFunctionValue = 0;
        double total = 0;
        double ef= Double.parseDouble(preference.get(EFFICIENCY_GAP.toString()));

        objectiveFunctionValue +=  ef * rateEfficiencyGap(d);
        total += ef;
        System.out.println("ef value: " + ef);

        double pope = Double.parseDouble(preference.get(POPULATION_EQUALITY.toString()));
        objectiveFunctionValue += pope * ratePopulationEquality(d);
        total += pope;
        System.out.println("pope value: " + objectiveFunctionValue);

        double comptivi = Double.parseDouble(preference.get(COMPETITIVENESS.toString()));
        objectiveFunctionValue += comptivi * rateCOMPETITIVENESS(d);
        total += comptivi;
        System.out.println("comptivi value: " + objectiveFunctionValue);

        double compact = Double.parseDouble(preference.get(COMPACTNESS.toString()));
        objectiveFunctionValue += compact * rateCompactnessBorder(d);
        total += compact;
        System.out.println("compact value: " + objectiveFunctionValue);

        double lw = Double.parseDouble(preference.get(LENGTH_WIDTH.toString()));
        objectiveFunctionValue += lw * rateCompactnessLenWid(d);
        total += lw;
        System.out.println("length/width value: " + objectiveFunctionValue);

        double objf = objectiveFunctionValue / total;
        System.out.println("OJF value: " + objf);
        return objf;
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
                iv_g += (gv - dv);
            } else if (dv > gv) {
                iv_g += gv;
                iv_d += (dv - gv);
            }
            tv += gv + dv;
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
        return 1.0 - 1.0 *(Math.abs(gv - dv)) / (gv + dv);
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
//        double length = d.getLength();
//        double width = d.getWidth();
//        return length / width;
        return Math.random();
    }

    public double rateCompactnessBorder(District d){
        double borderCount = d.getBorderPrecincts().size();
        double total = d.getPrecincts().size();
        return 1 - (1.0 * borderCount / total);
    }

    public double ratePolsbyPopper(District d){
        double AD = d.getData().getBoundary().getArea();
        double PD = d.getPerimeter();
        return AD*(4*Math.pow(Math.PI,3)/Math.pow(PD,4));
    }

    public double ratePopulationEquality(District d) {
        int dp = d.getData().getDemographic().getPopulation(RaceType.ALL);
        System.out.println("district pop: " + dp);
        System.out.println("NumDistrict: "+ state.getDistricts().size());
        System.out.println("total pop: " + state.getData().getDemographic().getPopulation(RaceType.ALL));
        int tp = state.getData().getDemographic().getPopulation(RaceType.ALL) / state.getDistricts().size();
        double rate = 1.0 * Math.abs(dp - tp) / tp;
        if (rate < 0.5) {
            return 1;
        } else if (rate > populationVariant) {
            return 0;
        } else {
            return (rate - 0.5) / (populationVariant - 0.5);
        }
    }


}
