package com.example.gerrymanderdemo.model;

import java.util.List;

public class PrecinctManager {
    // static variable single_instance of type Singleton
    private static PrecinctManager single_instance = null;

    // variable of type String
    public List<Precinct> p;

    // private constructor restricted to this class itself
    private PrecinctManager(){}


    // static method to create instance of Singleton class
    public static PrecinctManager getInstance()
    {
        if (single_instance == null)
            single_instance = new PrecinctManager();

        return single_instance;
    }
}
