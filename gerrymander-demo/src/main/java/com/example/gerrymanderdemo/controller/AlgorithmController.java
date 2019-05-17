package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.model.Algorithm;
import com.example.gerrymanderdemo.model.District;
import com.example.gerrymanderdemo.model.Precinct;
import com.example.gerrymanderdemo.model.State;
import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


@Controller
public class AlgorithmController {
    private Algorithm algorithm;

    @PostMapping(value = "/graphpartition", consumes = "application/json")
    public ResponseEntity<String> run(@RequestBody HashMap<String, String> preferences) {
        System.out.println(preferences);
        algorithm = new Algorithm(preferences, new State());
        State state = algorithm.graphPartition();
        return getDistrictPrecincts(state);
    }

    @PostMapping(value = "/graphpartition/once", consumes = "application/json")
    public ResponseEntity<String> runonce(@RequestBody HashMap<String, String> preferences) {
        if (algorithm == null) {
            algorithm = new Algorithm(preferences, new State());
        }
        State state = algorithm.graphPartitionOnce();
        return getDistrictPrecincts(state);
    }

    private ResponseEntity<String> getDistrictPrecincts(State state) {
        Collection<District> districts = state.getDistricts();
        JSONArray obj = new JSONArray();
        for (District district : districts) {
            Set<Precinct> precincts = district.getPrecincts();
            JSONArray ps = new JSONArray();
            for (Precinct precinct : precincts) {
                ps.put(precinct.getId());
            }
            obj.put(ps);
        }
        return ResponseEntity.ok(obj.toString());
    }

//    @PostMapping(value = "/district-precincts", consumes = "application/json")
//    public ResponseEntity<String> getDistrictPrecincts() {
//        if (algorithm == null) {
//            return ResponseEntity.status(400).body("Please first run algorithm");
//        }
//        else {
//            Collection<District> districts = algorithm.getState().getDistricts();
//        }
//    }




}
