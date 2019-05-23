package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.Service.UserService;
import com.example.gerrymanderdemo.model.Enum.*;
import com.example.gerrymanderdemo.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    UserService userService;

    @GetMapping(value = {"/", "/index", "/homepage"})
    public ModelAndView index(HttpSession session){
        User user = (User)session.getAttribute("user");
        ModelAndView mav;
        System.out.println("USER: "  + user);

        if(user != null && user.getUserType().equals(UserType.ADMIN)){
           System.out.println("user type: " + user.getUserType());
           mav = new ModelAndView("admin");
           List<User> users = userService.findAll();
           mav.addObject("listOfUsers", users);
           mav.addObject("UserTypes", UserType.values());
        } else{
            System.out.println("return index");
            System.out.println(user);
            mav = new ModelAndView("index");
            if(user != null)
                mav.addObject("user", user);
            mav.addObject("preferences", PreferenceType.values());
            mav.addObject("states", StateName.values());
            mav.addObject("communities", RaceType.values());
        }
        return mav;
    }

}