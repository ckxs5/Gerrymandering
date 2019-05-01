package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Enum.PreferenceType;

import java.util.Map;

public class Algorithm {
    private Map<String, String> preference;
    private State state;
    private ClusterManager clusterManager;
    float tempObjectiveFunctionValue;

    public Algorithm(Map<String, String> preference, State state, PrecinctService service) {
        this.preference = preference;
        this.state = state;
        this.clusterManager = new ClusterManager(service,
                PreferenceType.valueOf(preference.get(PreferenceType.COMMUNITY_OF_INTEREST.toString())),
                preference.get(PreferenceType.NUM_DISTRICTS));
    }

    public State graphPartician() {
        clusterManager.run();
        state.setDistricts(clusterManager.toDistricts());
        return state;
    }
    //TODO
    public State runAlgorithm(State s, Map<String, Number> userPreference){return null;}
    //TODO
    public float getOF(){return tempObjectiveFunctionValue;}



}
