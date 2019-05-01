package com.example.gerrymanderdemo.model;

import java.util.Map;

public class Algorithm {
    Map<String, Number> weights;
    State state;

    public Algorithm(Map<String, Number> weights, State state) {
        this.weights = weights;
        this.state = state;
    }


}
