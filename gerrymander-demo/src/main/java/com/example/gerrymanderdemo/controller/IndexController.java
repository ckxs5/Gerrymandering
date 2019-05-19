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
        User user = (User) session.getAttribute("user");
        System.out.println(session.getAttribute("user"));
        ModelAndView mav;

       if(user != null && user.getUserType().equals(UserType.ADMIN)){
           mav = new ModelAndView("admin");
           List<User> users = userService.findAll();
           System.out.println(users.get(0));
           mav.addObject("listOfUsers", users);
        } else {
            System.out.println("return index2");
            mav = new ModelAndView("index");
            mav.addObject("compactness", Compactness.values());
            mav.addObject("preferences", PreferenceType.values());
            mav.addObject("states", StateName.values());
            mav.addObject("communities", RaceType.values());
        }
        return mav;
    }

}