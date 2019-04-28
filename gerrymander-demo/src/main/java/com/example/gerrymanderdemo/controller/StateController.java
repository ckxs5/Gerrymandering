package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.StateService;
import com.example.gerrymanderdemo.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class StateController {
//    @Autowired
//    private StateService stateService;
//
//    @GetMapping("/state/{id}")
//    @ResponseBody
//    public String getState(@PathVariable String id, HttpSession session) {
//        State state = stateService.findById(id);
//        return null;
//    }
}
