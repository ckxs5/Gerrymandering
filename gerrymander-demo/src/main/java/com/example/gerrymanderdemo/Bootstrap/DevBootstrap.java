package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Service.*;
import com.example.gerrymanderdemo.model.*;
import com.example.gerrymanderdemo.model.Enum.StateName;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;

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
//        PrecinctPreprocesor preprocesor = new PrecinctPreprocesor(precinctService, demographicService, voteService, boundaryService, dataService, "/Users/brandonchan/Documents/GitHub/CSE308/gerrymander-demo/datafiles/MN/FINAL_MN_NEW.json");

        PrecinctManager.setInstance(precinctService);
        HashMap<Long, Precinct> precincts = PrecinctManager.getPrecincts(StateName.MINNESOTA);
        System.out.printf("Precinct %d loaded\n", precincts.size());
        System.out.println("precincts values"+precincts.values().toArray()[0].toString());

//        Construct District Manager
        DistrictManager.setInstance(districtService, dataService, demographicService, voteService, boundaryService);
//        test();
    }

    private HashMap<String, String> initPreferences() {
        HashMap<String, String> preferences = new HashMap<>();
        preferences.put("COMMUNITY_OF_INTEREST", "AFRICAN_AMERICAN");
        preferences.put("NUM_DISTRICTS", "8");
        preferences.put("MAJMIN_UP", "50");
        preferences.put("MAJMIN_LOW", "5");
        preferences.put("POPULATION_EQUALITY", "50");
        preferences.put("COMPETITIVENESS", "50");
        preferences.put("EFFICIENCY_GAP", "50");
        preferences.put("COMPACTNESS", "50");
        preferences.put("LENGTH_WIDTH", "50");


        return preferences;
    }

    private void test(){
        HashMap<String, String> preferences = initPreferences();
        Algorithm algorithm = new Algorithm(preferences, new State());
        algorithm.graphPartition();
//        algorithm.runTest();
        System.out.println("===================Starting make move===================");
        algorithm.setRedistrictingPlan();
        for (int i = 0; i < 10; i++) {
            algorithm.makeMove();
        }
    }

//    private void initData(){
//
//        Administrator administrator = new Administrator("admin@gmail.com", "cse308");
//        administratorRepository.save(administrator);
//    }
}
