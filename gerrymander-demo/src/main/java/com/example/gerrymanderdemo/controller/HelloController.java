package com.example.gerrymanderdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @ResponseBody
    @RequestMapping("/hello")
    public String Hello(){
        return "Hello World";
    }

    @ResponseBody
    @RequestMapping("/hello/nihao")
    public String nihao(){
        return "Ni Hao";
    }

}