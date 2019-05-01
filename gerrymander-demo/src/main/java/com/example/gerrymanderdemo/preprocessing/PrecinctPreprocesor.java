package com.example.gerrymanderdemo.preprocessing;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Data.Boundary;
import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Precinct;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PrecinctPreprocesor {
    private HashMap<String, ArrayList<String>> data;
    private PrecinctService precinctService;

    public PrecinctPreprocesor(PrecinctService precinctService, String filename) {
        this.precinctService = precinctService;
        prepareData(filename);
    }

    public void preparePrecinct() {
        for (int i = 0; i < data.get("NAME").size(); i++) {
            Precinct precinct = new Precinct();
            precinct.setName(data.get("NAME").get(i));
            precinct.setData(new Data(prepareVote(i), prepareDemograpgic(i), prepareBoundary(i)));
        }
    }

    private void prepareData(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
            String[] headers = reader.readLine().split(",");
            data = new HashMap<>();
            for (String field : headers) {
                data.put(field, new ArrayList<>());
            }
        } catch (FileNotFoundException e1) {
            System.out.printf("File not found : %s \n", e1.getMessage());
        } catch (IOException e2) {
            System.out.printf("Error when reading line from file %s\n", filename);
        }
    }

    private Vote prepareVote (int n) {
        int[] votes = new int[Party.values().length];
        for (Party party : Party.values()) {
            votes[party.ordinal()] = Integer.parseInt(data.get(party.name()).get(n));
        }
        return new Vote(votes);
    }

    private Demographic prepareDemograpgic (int n) {
        int[] demo = new int[RaceType.values().length];
        for (RaceType raceType : RaceType.values()) {
            demo[raceType.ordinal()] = Integer.parseInt(data.get(raceType.name()).get(n));
        }
        return new Demographic(demo);
    }

    private Boundary prepareBoundary (int n) {
        try {
            JSONObject json  = new JSONObject(data.get("BOUNDARY").get(n));
            return new Boundary(json.toString());
        } catch (JSONException ex) {
            System.out.printf("Error when getting boundary data from string : %s \n", ex);
            return null;
        }
    }
}

