package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Enum.StateName;

import java.util.ArrayList;
import java.util.List;

public class PrecinctManager {
    // static variable single_instance of type Singleton
    private static PrecinctManager single_instance = null;

    // variable of type String
    private static List<Precinct>[] precinctLists = new List[StateName.values().length];

    // private constructor restricted to this class itself
    private PrecinctManager(PrecinctService precinctService){
        for(StateName state : StateName.values()) {
            precinctLists[state.ordinal()] = precinctService.findAllByState(state);
        }
    }

    public static void setInstance(PrecinctService precinctService) {
        single_instance = new PrecinctManager(precinctService);
    }

    // static method to create instance of Singleton class
    public static PrecinctManager getInstance() throws NullPointerException
    {
        if (single_instance == null) {
            throw new NullPointerException();
        }
        return single_instance;
    }

    public static List<Precinct> getPrecincts(StateName state) {
        return precinctLists[state.ordinal()];
    }
}
