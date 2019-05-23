package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.Service.StateService;
import com.example.gerrymanderdemo.model.Enum.PreferenceType;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


public class BatchManager {
    private State orgState;
    private Collection<SummaryObject> summaries;
    private Map<String, String> userPreference;
    private PrecinctService precinctService;
    private StateService stateService;
    private Range<Double> majMinRange;

    public BatchManager(Map<String, String> userPreference, StateService stateService, PrecinctService precinctService){
        this.stateService = stateService;
        this.userPreference = userPreference;
        this.precinctService = precinctService;
        StateName state = StateName.valueOf(this.userPreference.get(PreferenceType.STATE_NAME.toString()));
        this.orgState = stateService.getOriginState(state);
        this.summaries = new ArrayList<>();
        this.majMinRange = new Range<>(
                Double.parseDouble(userPreference.get(PreferenceType.MAJMIN_LOW.toString())),
                Double.parseDouble(userPreference.get(PreferenceType.MAJMIN_UP.toString()))
                );
    }

    public void runBatch(){
        int times = Integer.parseInt(userPreference.get(PreferenceType.NUM_BATCH_RUN.toString()));
        for (int i = 0; i < times; i++) {
            runAlgorithm();
        }
    }

    private void runAlgorithm() {
        State s = new State(orgState);
        Algorithm algorithm = new Algorithm(userPreference, s);
        State intermediateState = algorithm.runAlgorithm();
        State stateWithId = stateService.saveState(intermediateState);
        int numMM = intermediateState.getNumMajMinDistricts(
                RaceType.valueOf(userPreference.get(PreferenceType.COMMUNITY_OF_INTEREST.toString())), majMinRange);
        double objValue = algorithm.getOF();
        SummaryObject so = new SummaryObject(stateWithId,objValue,numMM);
        this.summaries.add(so);
    }



}
