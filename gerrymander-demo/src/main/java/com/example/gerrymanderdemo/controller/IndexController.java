package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.model.Enum.*;
import com.example.gerrymanderdemo.model.User.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    @GetMapping(value = {"/", "/index"})
    public ModelAndView index(HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(session.getAttribute("user"));
        ModelAndView mav;

        if(user == null) {
            System.out.println("return login ");
            mav = new ModelAndView("login");
        }else if(user.getUserType().equals(UserType.ADMIN)){
            mav = new ModelAndView("admin");
        }
        else {
            System.out.println("return index ");
            mav = new ModelAndView("index");
            mav.addObject("compactness", Compactness.values());
            mav.addObject("weights", PreferenceType.values());
            mav.addObject("states", StateName.values());
            mav.addObject("communities", RaceType.values());
        }
        return mav;
    }

    @GetMapping("/homepage")
    public String homepage(){
        return "index";
    }

}