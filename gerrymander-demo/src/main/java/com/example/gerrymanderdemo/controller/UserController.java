package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.UserService;
import com.example.gerrymanderdemo.model.Exception.UserExistException;
import com.example.gerrymanderdemo.model.Exception.UserNotFoundException;
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

//    @GetMapping("/login")
//    public String getlogin() {
//        return "login";
//    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session){
        System.out.printf("Get email: %s and pass: %s\n", user.getEmail(), user.getPassword());
        try {
            session.setAttribute("user", userService.find(user.getEmail(), user.getPassword()));
            System.out.println("login successful");
            return new ResponseEntity<>((HttpStatus.OK));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error Message");
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

    @GetMapping("/user/{id}/delete")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }


    @PostMapping("/user/update")
    public ResponseEntity<String> update(@RequestBody User user) {
        try {
            userService.update(user);
            return ResponseEntity.ok("Success");
        } catch (UserNotFoundException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(404).body("User not found!");
        }
    }

//    @RequestMapping(value = "listOfUsers", method = RequestMethod.GET)
//    public String messages(Model model) {
//        model.addAttribute("messages", messageRepository.findAll());
//        return "message/list";
//    }

}