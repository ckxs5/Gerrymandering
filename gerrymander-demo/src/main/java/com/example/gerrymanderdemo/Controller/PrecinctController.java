package com.example.gerrymanderdemo.controller;


import com.example.gerrymanderdemo.JacksonSerializer.PrecinctDataSerializer;
import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Precinct;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
public class PrecinctController {
    @Autowired
    private PrecinctService precinctService;

    @GetMapping(value = "/precinct/{id}/data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getPrecinctData(@PathVariable Long id) {
        Optional<Precinct> obj = precinctService.findById(id);
        if (obj.isPresent()) {
            Precinct precinct = obj.get();
            System.out.printf("Found precinct:\n %s\n", precinct);
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(Precinct.class, new PrecinctDataSerializer());
            mapper.registerModule(module);
            try {
                return ResponseEntity.ok(mapper.writeValueAsString(precinct));
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
                return ResponseEntity.status(400).body("error");
            }
        } else {
            System.out.printf("Could not find entity with id %d \n", id);
            return ResponseEntity.status(404).body("Could not find entity");
        }
    }
}
