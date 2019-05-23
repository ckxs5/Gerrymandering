package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.JacksonSerializer.ClusterDataSerializer;
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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.java2d.loops.CustomComponent;

import java.util.*;


@Controller
public class AlgorithmController {
    private Algorithm algorithm;

    @PostMapping(value = "/init_algorithm", consumes = "application/json")
    public ResponseEntity<String> init(@RequestBody HashMap<String, String> preferences) {
       try {
           System.out.println("preferences: " + preferences);
           algorithm = new Algorithm(preferences, new State());
           System.out.println("algorithm: " + algorithm);
           return ResponseEntity.ok("Done");
       } catch (NumberFormatException ex) {
           return ResponseEntity.status(400).body("Please check your input value");
       }
    }

    @PostMapping(value = "/graphpartition", produces = "application/json")
    public ResponseEntity<String> run() {
        try {
            System.out.println("another algorithm: "+ algorithm);
            State state = algorithm.graphPartition();
            algorithm.setRedistrictingPlan();
            return getDistrictPrecincts(state.getDistricts());
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Please first initialize algorithm");
        }
    }


    @PostMapping(value = "/graphpartition/once", produces = "application/json")
    public ResponseEntity<String> runonce() {
        try {
            System.out.printf("Algorithm is AlgController is : %s\n", algorithm);
            algorithm.graphPartitionOnce();
            return getClusterPrecincts(algorithm.getClusterManager().getClusters());
        } catch (NotAnotherMoveException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Unable to have another move");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Please first initialize algorithm");
        }
    }

    @GetMapping(value = "/district/{id}/data", produces = "application/json")
    public ResponseEntity<String> getDistrictById(@PathVariable Long id) {
        try {
            System.out.printf("Request to get data for district : %d \n", id);
            District district = algorithm.getState().getDistrictById(id);
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
        } catch (NullPointerException ex) {
            return getClusterData(Integer.parseInt(id.toString()));
        }
    }

    @PostMapping(value = "/simulating_annealing", produces = "application/json")
    public ResponseEntity<String> simulatingAnnealing() {
        try {
            System.out.println("Cheryl==========="+algorithm);
            Move move = algorithm.makeMove();
            JSONObject obj = new JSONObject();
            System.out.println("Algorithm move: " + move);
             obj.put("to", move.getTo().getId());
            obj.put("from", move.getFrom().getId());
            obj.put("p", move.getPrecinct().getId());
            obj.put("message", String.format("Objective Function value: %f.5 ", algorithm.getOF()));
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

    private ResponseEntity<String> getClusterPrecincts(List<Cluster> clusters) {
        try {
            JSONObject object = new JSONObject();
            for (int i = 0; i < clusters.size(); i++) {
                Set<Precinct> precincts = clusters.get(i).getPrecincts();
                JSONArray ps = new JSONArray();
                for (Precinct p : precincts) {
                    District fake = new District();
                    fake.setId(new Long(i));
                    p.setDistrict(fake, false);
                    ps.put(p.getId());
                }
                object.put("" + i, ps);
                object.put("message", String.format("%d Clusters", clusters.size()));
            }
            System.out.printf("Return %d of Clusters with its precinctIds. \n", clusters.size());
            return ResponseEntity.ok(object.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Cannot convert to JSON when getting cluster precincts");
        }
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
            obj.put("message", String.format("%d Districts", districts.size()));
            System.out.printf("Return %d of Districts with its precinctIds. \n", obj.length());
            return ResponseEntity.ok(obj.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(400).body("Cannot convert to JSON when getting district precincts");
        }
    }

    @GetMapping(value = "/getsummaries", produces = "application/json")
    public ResponseEntity<String> getSummaries() {
        Long[] ids = {new Long(15), new Long(14), new Long(13), new Long(12),
                new Long(11), new Long(10), new Long(9), new Long(8),
                new Long(7) , new Long(6), new Long(5)};

        List<SummaryObject> summaryObjects = new ArrayList<>();
        for (Long id : ids) {
            State state = StateManager.getInstance().findById(id);
            if (state == null)
                System.out.printf("State Not found !!! : id : %d", id);
            summaryObjects.add(new SummaryObject(state, Math.random() + 0.3, 1));
        }
        JSONArray arr = new JSONArray();
        for (SummaryObject object : summaryObjects) {
            arr.put(object.toJSONObject());
        }
        return ResponseEntity.ok(arr.toString());
    }

    @GetMapping(value = "cluster/{index}/data")
    public ResponseEntity<String> getClusterData(@PathVariable int index) {
        try {
            Cluster c = algorithm.getClusterManager().getClusters().get(index);
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(Cluster.class, new ClusterDataSerializer());
            mapper.registerModule(module);
            try {
                System.out.printf("Returning cluster %d data.\n", index);
                return ResponseEntity.ok(mapper.writeValueAsString(c));
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
                return ResponseEntity.status(400).body("error");
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(404).body("No cluster found");
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<String> save(){
        StateManager.getInstance().save(algorithm.getState());
        return ResponseEntity.ok("Saved");
    }

    @GetMapping(value = "/state/{id}/load")
    public ResponseEntity<String> load(@PathVariable Long id){
        State state = StateManager.getInstance().findById(id);
        if (state == null) {
            return ResponseEntity.status(404).body("State not found");
        }
        this.algorithm.setState(state);
        return getDistrictPrecincts(state.getDistricts());
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
