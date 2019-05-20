package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.JacksonSerializer.DistrictDataSerializer;
import com.example.gerrymanderdemo.JacksonSerializer.PrecinctDataSerializer;
import com.example.gerrymanderdemo.model.*;
import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.model.Exception.NotAnotherMoveException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mysql.cj.xdevapi.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


@Controller
public class AlgorithmController {
    private Algorithm algorithm;

    @PostMapping(value = "/init_algorithm", consumes = "application/json")
    public ResponseEntity<String> init(@RequestBody HashMap<String, String> preferences) {
        System.out.println("preferences: " + preferences);
        algorithm = new Algorithm(preferences, new State());
        System.out.println("algorithm: " + algorithm);
        return ResponseEntity.ok("Done");
    }

    @PostMapping(value = "/graphpartition", produces = "application/json")
    public ResponseEntity<String> run() {
        try {
            System.out.println("another algorithm: "+ algorithm);
            State state = algorithm.graphPartition();
            return getDistrictPrecincts(state.getDistricts());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Please first initialize algorithm");
        }
    }

    @PostMapping(value = "/graphpartition/once", produces = "application/json")
    public ResponseEntity<String> runonce() {
        try {
            State state = algorithm.graphPartitionOnce();
            return getDistrictPrecincts(state.getDistricts());
        } catch (NotAnotherMoveException ex) {
            return ResponseEntity.status(400).body("Unable to have another move");
        } catch (NullPointerException ex) {
            return ResponseEntity.status(400).body("Please first initialize algorithm");
        }
    }

    @GetMapping(value = "/district/{id}/data", produces = "application/json")
    public ResponseEntity<String> getDistrictById(@PathVariable Long id) {
        System.out.printf("Request to get data for district : %d \n", id);
        District district = algorithm.getState().getDistrictById(id);
        if (district != null) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(District.class, new DistrictDataSerializer());
            mapper.registerModule(module);
            try {
                System.out.println("got");
                return ResponseEntity.ok(mapper.writeValueAsString(district));
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
                return ResponseEntity.status(400).body("error");
            }
        } else {
            System.out.printf("Could not find entity with id %d \n", id);
            return ResponseEntity.status(404).body("Could not find entity");
        }
    }

    @PostMapping(value = "/simulating_annealing", produces = "application/json")
    public ResponseEntity<String> simulatingAnnealing() {
        try {
            algorithm.setRedistrictingPlan();
            Move move = algorithm.makeMove();
            JSONObject obj = new JSONObject();
            obj.put("to", move.getTo().getId());
            obj.put("from", move.getFrom().getId());
            obj.put("p", move.getPrecinct().getId());
            return ResponseEntity.ok(obj.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Cannot convert to JSON when doing simulating_annealing");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Please first initial Algorithm");
        }
    }

    @GetMapping(value = "/getmmd", produces = "application/json")
    public ResponseEntity<String> getMajMinDistrictBorders(){
        return getDistrictPrecincts(algorithm.getState().getMMDistricts(algorithm.getCommunityOfInterest(), algorithm.getRange()));
    }

    private ResponseEntity<String> getDistrictPrecincts(Collection<District> districts) {
        try {
            JSONObject obj = new JSONObject();
            for (District district : districts) {
                Set<Precinct> precincts = district.getPrecincts();
                JSONArray ps = new JSONArray();
                for (Precinct precinct : precincts) {
                    ps.put(precinct.getId());
                }
                obj.put(district.getId().toString(), ps);
            }
            System.out.printf("Return %d of Districts with its precinctIds. \n", obj.length());
            return ResponseEntity.ok(obj.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Cannot convert to JSON when getting district precincts");
        }
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
