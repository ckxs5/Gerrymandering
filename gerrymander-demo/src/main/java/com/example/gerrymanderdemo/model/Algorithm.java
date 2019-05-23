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
    private Range<Double> range;
    private RaceType communityOfInterest;
    private ClusterManager clusterManager;
    private float tempObjectiveFunctionValue;
    private District currentDistrict = null;
    private HashMap<Long, Double> currentScores;//TODO: Map district to its score
    @Value("${population.range.variant}")
    private double populationVariant;


    public Algorithm(Map<String, String> preference, State state) throws NumberFormatException{
        this.communityOfInterest = RaceType.valueOf(preference.get(PreferenceType.COMMUNITY_OF_INTEREST.toString()));
        this.preference = preference;
        this.state = state;
        this.range = new Range<>(Double.parseDouble(preference.get("MAJMIN_LOW")) / 100 ,Double.parseDouble(preference.get("MAJMIN_UP")) / 100);
        this.clusterManager = new ClusterManager(
                communityOfInterest,
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
        return state;
    }

    public void setRedistrictingPlan(){
        for (Precinct p : PrecinctManager.getPrecincts(StateName.MINNESOTA).values()) {
            p.setDistrict(p.getDistrict(), true);
        }
        currentScores = new HashMap<>();
        System.out.println("Set Redistrict Plan!!!!!!!!!!!!");
        for (District d: state.getDistricts()){
            currentScores.put(d.getId(), rateDistrict(d));
        }
    }

    //TODO
    public State runAlgorithm(){return null;}

    public Move runTest() {
        MajMinManager majMinManager = new MajMinManager(
                getState(),
                RaceType.valueOf(preference.get("COMMUNITY_OF_INTEREST")),
                 range.getUpperBound(),
                 range.getLowerBound(),
                true);
        District district = majMinManager.getBestCandidate();
        System.out.printf("District get from mm Manager is %s \n", district);
        Move move = majMinManager.moveFromDistrict();

        System.out.println(move.getTo());
        System.out.println(move.getFrom());
        System.out.println(move.getPrecinct());
        return move;
    }


    public double getOF(){
        double sum = 0;
        for(double score : currentScores.values()) {
            sum += 0;
        }
        return sum / state.getDistricts().size();
    }


    public Move makeMove(){
        System.out.printf("Before makeMove district number :%d \n", state.getDistricts().size());
        if (currentDistrict == null){
            currentDistrict = getWorstDistrict();
        }
        Move m = getMoveFromDistrict(currentDistrict);
        if (m == null){
            System.out.printf("After makeMove district number :%d \n", state.getDistricts().size());
            return makeMove_secondary();
        }
        System.out.printf("After makeMove district number :%d \n", state.getDistricts().size());
        return m;
    }

    //This is similar to makeMove, but reverse to put neighbor precinct to startDistrict and check
    public Move makeMove_secondary() {
        System.out.printf("Before makeMove_secondary district number :%d \n", state.getDistricts().size());
        List<District> districts = getWorstDistricts();//TODO:getWorstDistricts()
        districts.remove(0);
        while (districts.size() > 0) {
            District startDistrict = districts.remove(0);
            Move m = getMoveFromDistrict(startDistrict);
            if (m != null) {
                System.out.printf("After makeMove_secondary district number :%d \n", state.getDistricts().size());
                return m;
            }
        }
        System.out.printf("After makeMove_secondary  returning null district number :%d \n", state.getDistricts().size());
        return null;
    }

    // TODO: returns a list of districts sorted from worst to best
    public List<District> getWorstDistricts() {
        System.out.printf("Before getWorstDistricts district number :%d \n", state.getDistricts().size());
        state.getDistricts().sort((o1, o2) -> {
            double rate1 = rateDistrict(o1);
            double rate2 = rateDistrict(o2);
            return Double.compare(rate1, rate2);
        });
        System.out.printf("After getWorstDistricts district number :%d \n", state.getDistricts().size());
        return new ArrayList<>(state.getDistricts());
    }

    public District getWorstDistrict() {
        System.out.printf("Before getWorstDistrict district number :%d \n", state.getDistricts().size());
        District worstDistrict = null;
        double minScore = Double.POSITIVE_INFINITY;
        System.out.println("Get Districts: " + state.getDistricts());
        for (District d : state.getDistricts()) {//TODO: getDistricts()
            double score = rateDistrict(d);
            currentScores.put(d.getId(), score);
            System.out.println("Score: " + score);
            if (score < minScore) {
                worstDistrict = d;
                minScore = score;
            }
        }
        System.out.printf("After getWorstDistrict district number :%d \n", state.getDistricts().size());
        return worstDistrict;
    }

    public Move getMoveFromDistrict(District startDistrict){
        System.out.printf("Before getMoveFromDistrict district number :%d \n", state.getDistricts().size());
        List<District> ns = new ArrayList<>(startDistrict.getNeigbours());
        for (District d : ns) {
            List<Move> moves = startDistrict.constructMovesWithToDistrict(d);
            while (moves.size() > 0) {
                Move move = moves.remove(0);
                System.out.printf("getMoveFromDistrict got move %s\n", move);
                move = testMove(move);
                if (move != null) {

                    return move;
                }
            }

            moves = d.constructMovesWithToDistrict(startDistrict);
            while (moves.size() > 0) {
                Move move = moves.remove(0);
                System.out.printf("getMoveFromDistrict got move %s\n", move);
                move = testMove(move);
                if (move != null) {
                    return move;
                }
            }
        }

        System.out.printf("After getMoveFromDistrict district number :%d \n", state.getDistricts().size());
        return null;
    }

    private Move testMove(Move move) {
        System.out.printf("Before testMove district number :%d \n", state.getDistricts().size());
        if (!checkContiguity(move.getPrecinct(), move.getFrom())) {
            return null;
        }
//        System.out.printf("Current score in test move %s \n", currentScores);
//        System.out.printf("Current score1 %f score2 %f \n", currentScores.get(move.getTo().getId()), currentScores.get(move.getFrom().getId()));
//        System.out.printf("Current move to %d \n", move.getTo().getId());
//        System.out.printf("Current move from %d \n", move.getFrom().getId());
//        if(currentScores.get(move.getTo().getId()) == null) {
//            System.out.printf("Current score contains key %s\n", currentScores.containsKey(move.getTo().getId()));
//            System.out.printf("State contains key %s\n", state.getDistricts().contains(move.getTo()));
//        }
//        if(currentScores.get(move.getFrom().getId()) == null) {
//            System.out.printf("Current score contains key %s\n", currentScores.containsKey(move.getFrom().getId()));
//            System.out.printf("State contains key %s\n", state.getDistricts().contains(move.getFrom()));
//        }
        double initial_score = currentScores.get(move.getTo().getId()) + currentScores.get(move.getFrom().getId());
        move.execute();
        double to_score = rateDistrict(move.getTo());
        double from_score = rateDistrict(move.getFrom());
        double final_score = (to_score + from_score);
        double change = final_score - initial_score;
        if (change <= 0) {
            move.undo();
            System.out.printf("After testMove district number :%d \n", state.getDistricts().size());
            return null;
        }
        currentScores.put(move.getTo().getId(), to_score);
        currentScores.put(move.getFrom().getId(), from_score);
        System.out.printf("After testMove district number :%d \n", state.getDistricts().size());
        return move;
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

    public HashMap<Long, Double> getCurrentScores() {
        return currentScores;
    }

//    public void setCurrentScores(HashMap<Long, Double> currentScores) {
//        this.currentScores = currentScores;
//    }

    public Range<Double> getRange() {
        return range;
    }

    public void setRange(Range<Double> range) {
        this.range = range;
    }

    public RaceType getCommunityOfInterest() {
        return communityOfInterest;
    }

    public void setCommunityOfInterest(RaceType communityOfInterest) {
        this.communityOfInterest = communityOfInterest;
    }

    public double getPopulationVariant() {
        return populationVariant;
    }

    public void setPopulationVariant(double populationVariant) {
        this.populationVariant = populationVariant;
    }

    //TODO:check contiguity for moving precinct p out of district d
    //returns true if contiguous
    private boolean checkContiguity(Precinct p, District d){
//        Set<Long> neighborIDs = p.getNeighborIDs();
        Set<Precinct> neighbors = p.getNeighbors();
        HashSet<Precinct> neededPrecincts = new HashSet<Precinct>();
        HashSet<Precinct> neighborToExplore = new HashSet<Precinct>();//potential sources of exploration
        HashSet<Precinct> exploreNeighbors = new HashSet<Precinct>();// neighbors already explored
        exploreNeighbors.add(p);// add the precinct being moved, to ensure it won't be used
        // if a neighbor is in the district that's losing a precinct, we need to make sure they're still contiguous
        for (Precinct n : neighbors) {
            // if neighbor is in the district we're losing from
//            if ((redistrictingPlan.get(id)).equals(d.getId())) {
            if(n.getDistrict().equals(d)) {
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
            for (Precinct nn : n.getNeighbors()) {
                //we only care about neighbors in our district,d
//                if (redistrictingPlan.get(id).equals(d.getId())) {
                if (nn.getDistrict().equals(d)) {
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

        double pope = Double.parseDouble(preference.get(POPULATION_EQUALITY.toString()));
        objectiveFunctionValue += pope * ratePopulationEquality(d);
        total += pope;

        double comptivi = Double.parseDouble(preference.get(COMPETITIVENESS.toString()));
        objectiveFunctionValue += comptivi * rateCOMPETITIVENESS(d);
        total += comptivi;

        double compact = Double.parseDouble(preference.get(COMPACTNESS.toString()));
        objectiveFunctionValue += compact * rateCompactnessBorder(d);
        total += compact;

        double lw = Double.parseDouble(preference.get(LENGTH_WIDTH.toString()));
        objectiveFunctionValue += lw * rateCompactnessLenWid(d);
        total += lw;

        double objf = objectiveFunctionValue / total;
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
        System.out.printf("District in rateEfficiencyGap is %s \n", d);
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
        return 1.0 - 1.0  * (Math.abs(gv - dv)) / (gv + dv);
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
        if (rate < 0.05) {
            return 1;
        } else if (rate > populationVariant) {
            return 0;
        } else {
            return (rate - 0.05) / (populationVariant - 0.05);
        }
    }


}
