package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.UserService;
import com.example.gerrymanderdemo.model.Enum.UserType;
import com.example.gerrymanderdemo.model.Response.ErrorResponse;
import com.example.gerrymanderdemo.model.Response.OKResponse;
import com.example.gerrymanderdemo.model.Response.OKUserResponse;
import com.example.gerrymanderdemo.model.Response.Response;
import com.example.gerrymanderdemo.model.User.User;
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
    public ResponseEntity<Response> login(@RequestBody User user, HttpSession session){
        System.out.printf("Get email: %s and pass: %s\n", user.getEmail(), user.getPassword());
        user = userService.find(user.getEmail(), user.getPassword());
        if (user != null) {
            session.setAttribute("user", user);
            System.out.printf("UserType: %s login\n", user);
            return ResponseEntity.ok(new OKUserResponse(user));
        }
        else return ResponseEntity.ok(new ErrorResponse("Could not found user"));
    }

    @PostMapping("/signup")
    public ResponseEntity<Response> signup(@RequestBody User user) {
        user.setUserType(UserType.USER);
        System.out.printf("Adding User to: %s login\n", user);
        user = userService.addUser(user);
        if (user == null)
            return ResponseEntity.ok(new ErrorResponse("UserType Exist"));
        else
            return ResponseEntity.ok(new OKResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpSession session) {
        System.out.println("logout");
        session.removeAttribute("user");
        return ResponseEntity.ok(new OKResponse());
    }

    @GetMapping("/homepage")
    public String homepage(){
        return "index";
    }
}