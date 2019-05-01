package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.UserService;
import com.example.gerrymanderdemo.model.Exception.UserExistException;
import com.example.gerrymanderdemo.model.Response.OKResponse;
import com.example.gerrymanderdemo.model.Response.Response;
import com.example.gerrymanderdemo.model.User.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getlogin() {
        return "login";
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session){
        System.out.printf("Get email: %s and pass: %s\n", user.getEmail(), user.getPassword());
        try {
            session.setAttribute("user", userService.find(user.getEmail(), user.getPassword()));
            return new ResponseEntity<>((HttpStatus.OK));
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            userService.addUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserExistException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpSession session) {
        System.out.println("logout");
        session.removeAttribute("user");
        return ResponseEntity.ok(new OKResponse());
    }

}