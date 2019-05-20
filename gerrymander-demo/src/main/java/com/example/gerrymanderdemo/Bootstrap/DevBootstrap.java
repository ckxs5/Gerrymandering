package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Service.*;
import com.example.gerrymanderdemo.model.*;
import com.example.gerrymanderdemo.model.Data.Boundary;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.preprocessing.PrecinctConstructor;
import com.example.gerrymanderdemo.preprocessing.PrecinctPreprocesor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private PrecinctService precinctService;
    private DemographicService demographicService;
    private VoteService voteService;
    private BoundaryService boundaryService;
    private DataService dataService;
    private DistrictService districtService;

    public DevBootstrap(PrecinctService precinctService, DemographicService demographicService, VoteService voteService, BoundaryService boundaryService, DataService dataService, DistrictService districtService) {
        this.precinctService = precinctService;
        this.demographicService = demographicService;
        this.voteService = voteService;
        this.boundaryService = boundaryService;
        this.dataService = dataService;
        this.districtService = districtService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // DO NOT UNCOMMENT THIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        PrecinctPreprocesor preprocesor = new PrecinctPreprocesor(precinctService, demographicService, voteService, boundaryService, dataService, "C:\\Users\\jimmy\\OneDrive\\Desktop\\Spring Programming\\CSE308\\gerrymander-demo\\datafiles\\MN\\precinct_data.json");

        PrecinctManager.setInstance(precinctService);
        HashMap<Long, Precinct> precincts = PrecinctManager.getPrecincts(StateName.MINNESOTA);
        System.out.printf("Precinct %d loaded\n", precincts.size());
        System.out.println(precincts.values().toArray()[0].toString());



        //Construct District Manager
        DistrictManager.setInstance(districtService, dataService, demographicService, voteService, boundaryService);
        test();
    }

    private HashMap<String, String> initPreferences() {
        HashMap<String, String> preferences = new HashMap<>();
        preferences.put("COMMUNITY_OF_INTEREST", "AFRICAN_AMERICAN");
        preferences.put("NUM_DISTRICTS", "8");

        return preferences;
    }

    private void test(){
        HashMap<String, String> preferences = initPreferences();
        Algorithm algorithm = new Algorithm(preferences, new State());
        algorithm.graphPartition();


    }

//    private void initData(){
//
//        Administrator administrator = new Administrator("admin@gmail.com", "cse308");
//        administratorRepository.save(administrator);
//    }
}
