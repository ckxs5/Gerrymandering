package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.UserService;
import com.example.gerrymanderdemo.model.Guest;
import com.example.gerrymanderdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
public class HelloController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session){
        User user = userService.find(email, password);
        session.setAttribute("user", user);
        System.out.println(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestParam("email") String email, @RequestParam("username") String username, @RequestParam("password") String password) {
        Guest guest = new Guest(username, email, password);
        guest = userService.addGuest(guest);
        return ResponseEntity.ok(guest);
    }

    @GetMapping("/homepage")
    public String homepage(){
        return "index";
    }

}