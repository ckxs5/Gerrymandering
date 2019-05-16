package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.model.Algorithm;
import com.example.gerrymanderdemo.model.State;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;


@Controller
public class AlgorithmController {

    @PostMapping(value = "/graphpartisian", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<String> run(@RequestBody HashMap<String, String> preferences) {
        System.out.println("Hello Haofeng");
        System.out.println(preferences);
        Algorithm algorithm = new Algorithm(preferences, new State());
        State state = algorithm.graphPartisian();
        System.out.println(state.getDistricts().toArray().length);
        return null;
    }


}
