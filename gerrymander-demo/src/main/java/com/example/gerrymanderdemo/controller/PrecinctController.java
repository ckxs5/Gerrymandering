package com.example.gerrymanderdemo.controller;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Precinct;
import com.example.gerrymanderdemo.model.Response.Response;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PrecinctController {
    public PrecinctController() {
    }

    @RequestMapping("/precinct/{id}/data")
    @ResponseBody
    //TODO Subjected to change for production
    public String getPrecinct(@PathVariable Long id) {
        Precinct precinct = new Precinct();
        Data data = new Data();
        int[] demo = new int[RaceType.values().length];
        int[] vote = new int [Party.values().length];
        for (int i = 0; i < demo.length; i++)
            demo[i] = (int)(Math.random() * 1000000 + 1000000);
        for (int i = 0; i < vote.length; i++)
            vote[i] = (int)(Math.random() * 1000000 + 1000000);
        data.setVoteData(new Vote(vote));
        data.setDemographic(new Demographic(demo));
        precinct.setData(data);
        precinct.setName("Test Precinct 1");
        System.out.println(precinct.toJSONObject().toString());
        return precinct.toJSONObject().toString();
    }

}
