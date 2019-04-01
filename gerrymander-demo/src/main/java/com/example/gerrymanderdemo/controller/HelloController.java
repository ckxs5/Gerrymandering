package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.UserService;
import com.example.gerrymanderdemo.model.Guest;
import com.example.gerrymanderdemo.model.Response.ErrorResponse;
import com.example.gerrymanderdemo.model.Response.OKResponse;
import com.example.gerrymanderdemo.model.Response.OKUserResponse;
import com.example.gerrymanderdemo.model.Response.Response;
import com.example.gerrymanderdemo.model.User;
import org.apache.tomcat.util.json.JSONParser;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
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
    public String index(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null)
            return "login";
        else
            return "index";
    }

    @GetMapping("/login")
    public String getlogin() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody User user, HttpSession session){
        System.out.printf("Get email: %s and pass: %s\n", user.getEmail(), user.getPassword());
        user = userService.find(user.getEmail(), user.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            System.out.printf("User: %s login\n", user);
            return ResponseEntity.ok(new OKUserResponse(user));
        }
        else return ResponseEntity.ok(new ErrorResponse("Could not found user"));
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody User user) {
        user = userService.addGuest(user);
        if (user == null)
            return ResponseEntity.ok(new ErrorResponse("User Exist"));
        else
            return ResponseEntity.ok(new OKResponse());
    }

    @GetMapping
    public ResponseEntity<Response> logout(HttpSession session) {
        session.setAttribute("user", null);
        return ResponseEntity.ok(new OKResponse());
    }


    @GetMapping("/homepage")
    public String homepage(){
        return "index";
    }

}