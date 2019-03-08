package com.example.gerrymanderdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HelloController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/index")
    public String homepage(){
        return "index";
    }
}