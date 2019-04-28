package com.example.gerrymanderdemo.preprocessing;


import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PrecinctPreprocesor {
    private HashMap<String, ArrayList<String>> data;

    private void prepareData (File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] headers = reader.readLine().split(",");
            data = new HashMap<>();
            for (String field : headers)
                data.put(field, new ArrayList<>());
        } catch (FileNotFoundException e1) {
            System.out.printf("File not found : %s", e1.getMessage());
        } catch (IOException e2) {
            System.out.printf("Error when reading line from file %s", file.getName());
        }
    }

    private Vote prepareVote (int n) {
        int[] votes = new int[Party.values().length];
        for (Party party : Party.values())
            votes[party.ordinal()] = Integer.parseInt(data.get(party.name()).get(n));
        return new Vote(votes);
    }

    private Demographic prepareDemograpgic (int n) {
        int[] demo = new int[RaceType.values().length];
        for (RaceType raceType : RaceType.values())
            demo[raceType.ordinal()] = Integer.parseInt(data.get(raceType.name()).get(n));
        return new Demographic(demo);
    }

//    private Boundary prepareBoundary (int n) {
//        try{
//            JSONObject geoJSON = new JSONObject();
//        } catch (JSONException ex) {
//
//        }
//    }
}

