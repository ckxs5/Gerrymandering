package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Enum.PreferenceType;

import java.util.Map;

public class Algorithm {
    private Map<String, Number> preference;
    private State state;
    private ClusterManager clusterManager;


    public Algorithm(Map<String, Number> preference, State state, PrecinctService service) {
        this.preference = preference;
        this.state = state;
        this.clusterManager = new ClusterManager(service, preference.get(PreferenceType.NUM_DISTRICTS.toString()).intValue());
    }

    public State graphPartician() {
        clusterManager.run();
        state.setDistricts(clusterManager.toDistricts());
        return state;
    }




}
