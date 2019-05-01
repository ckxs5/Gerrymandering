package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.StateService;

import java.util.Collection;
import java.util.Map;


public class BatchManager {
    private State orgState;
    private Algorithm algorithm;
    private Collection<SummaryObject> summaries;
    private Map<String, Number> userPreference;
    private StateService stateService;

    public BatchManager(Map<String, Number> userPreference, StateService stateService, Algorithm algorithm){
        this.stateService = stateService;
        this.userPreference = userPreference;
        Number state = this.userPreference.get("stateName");
        this.orgState = stateService.getOriginState(state);
        this.algorithm = algorithm;
        this.summaries = null;
    }

    public void runBatch(){
        State s = new State(orgState);
        State intermediateState = algorithm.runAlgorithm(s,userPreference);
        State stateWithId = stateService.saveState(intermediateState);
        Number numMM = userPreference.get("number of districts");
        float objValue = algorithm.getOF();
        SummaryObject so = new SummaryObject(stateWithId,objValue,numMM.intValue());
        addSummary(so);
    }

    public void addSummary(SummaryObject so){
        summaries.add(so);
    }


}
