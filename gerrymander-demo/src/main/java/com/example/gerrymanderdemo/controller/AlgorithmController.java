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
        try {
            State state = algorithm.graphPartitionOnce();
            return getDistrictPrecincts(state);
        } catch (NotAnotherMoveException ex) {
            return ResponseEntity.status(400).body("Unable to have another move");
        }
    }

    @GetMapping(value = "/district/{id}/data")
    public ResponseEntity<String> getDistrictById(@PathVariable Long id) {
        System.out.printf("Request to get data for precinct : %d \n", id);
        District district = algorithm.getState().getDistrictById(id);
        if (district != null) {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(District.class, new DistrictDataSerializer());
            mapper.registerModule(module);
            try {
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
