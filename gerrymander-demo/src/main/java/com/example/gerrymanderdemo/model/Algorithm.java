package com.example.gerrymanderdemo.model;

import java.util.Map;

public class Algorithm {
    Map<String, Number> userPreference;
    State state;
    float tempObjectiveFunctionValue;

    public Algorithm(Map<String, Number> userPreference, State state) {
        this.userPreference = userPreference;
        this.state = state;
    }
    //TODO
    public State runAlgorithm(State s, Map<String, Number> userPreference){return null;}
    //TODO
    public float getOF(){return tempObjectiveFunctionValue;}

}
