package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Service.*;
import com.example.gerrymanderdemo.model.ClusterManager;
import com.example.gerrymanderdemo.model.Data.Boundary;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Precinct;
import com.example.gerrymanderdemo.preprocessing.PrecinctConstructor;
import com.example.gerrymanderdemo.preprocessing.PrecinctPreprocesor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private PrecinctService precinctService;
    private DemographicService demographicService;
    private VoteService voteService;
    private BoundaryService boundaryService;
    private DataService dataService;

    public DevBootstrap(PrecinctService precinctService, DemographicService demographicService, VoteService voteService, BoundaryService boundaryService, DataService dataService) {
        this.precinctService = precinctService;
        this.demographicService = demographicService;
        this.voteService = voteService;
        this.boundaryService = boundaryService;
        this.dataService = dataService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        PrecinctPreprocesor preprocesor = new PrecinctPreprocesor(precinctService, demographicService, voteService, boundaryService, dataService, "C:\\Users\\jimmy\\OneDrive\\Desktop\\Spring Programming\\CSE308\\gerrymander-demo\\datafiles\\MN\\precinct_data.json");
//        List<Precinct> precincts = precinctService.findAll();
//        System.out.println(precincts.size());
    }

//    private void initData(){
//
//        Administrator administrator = new Administrator("admin@gmail.com", "cse308");
//        administratorRepository.save(administrator);
//    }
}
