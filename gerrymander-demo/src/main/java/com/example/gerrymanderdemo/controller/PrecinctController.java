package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Precinct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PrecinctController {
    @Autowired
    private PrecinctService precinctService;

//    @RequestMapping(value = "/precinct/{id}/data", method = RequestMethod.GET, produces = "application/json")
//    @ResponseBody
//    //TODO Subjected to change for production
//    public String getPrecinctData(@PathVariable Long id) {
//        precinctService.findById(id).ifPresent(x -> {
//
//        });
//        return precinct.toJSONObject().toString();
//    }
}
