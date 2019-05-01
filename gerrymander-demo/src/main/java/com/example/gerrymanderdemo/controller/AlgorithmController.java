package com.example.gerrymanderdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;


@Controller
public class AlgorithmController {

    @PostMapping(value = "/setweights", consumes = "application/json")
    @ResponseBody
    public String setWeights(@RequestBody HashMap<String, Number> weights) {
        System.out.println("Hello Haofeng");
        System.out.println(weights);
        return null;
    }
}
