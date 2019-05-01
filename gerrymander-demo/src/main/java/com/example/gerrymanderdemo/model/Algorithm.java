package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Enum.PreferenceType;
import com.example.gerrymanderdemo.model.Enum.RaceType;

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
                RaceType.valueOf(preference.get(preference.get(PreferenceType.NUM_DISTRICTS.toString()))),
                Integer.parseInt(preference.get(PreferenceType.NUM_DISTRICTS.toString())));
    }

    public State graphPartician() {
        clusterManager.run();
        state.setDistricts(clusterManager.toDistricts());
        return state;
    }
    //TODO
    public State runAlgorithm(){return null;}
    //TODO
    public float getOF(){return tempObjectiveFunctionValue;}



}
