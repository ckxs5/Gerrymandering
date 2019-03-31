package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.UserService;
import com.example.gerrymanderdemo.model.Guest;
import com.example.gerrymanderdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;


@Controller
public class HelloController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String getlogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session){
        System.out.printf("Get email: %s and pass: %s\n", email, password);
        User user = userService.find(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            System.out.printf("User is %s login\n", user);
            return "index";
        }
        else return "login";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam("email") String email, @RequestParam("password") String password) {
        Guest guest = new Guest("Unknown", email, password);
        userService.addGuest(guest);
        return "login";
    }


    @GetMapping("/homepage")
    public String homepage(){
        return "index";
    }

}