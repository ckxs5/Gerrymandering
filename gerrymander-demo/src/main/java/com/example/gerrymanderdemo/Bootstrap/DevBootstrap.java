package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Service.*;
import com.example.gerrymanderdemo.model.*;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
    private StateService stateService;

    public DevBootstrap(PrecinctService precinctService, DemographicService demographicService, VoteService voteService, BoundaryService boundaryService, DataService dataService, DistrictService districtService,StateService stateService) {
        this.precinctService = precinctService;
        this.demographicService = demographicService;
        this.voteService = voteService;
        this.boundaryService = boundaryService;
        this.dataService = dataService;
        this.districtService = districtService;
        this.stateService = stateService;
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
        StateManager.setInstance(stateService, dataService, demographicService, voteService, boundaryService);

//        test();
    }

    private HashMap<String, String> initPreferences() {
        HashMap<String, String> preferences = new HashMap<>();
        preferences.put("COMMUNITY_OF_INTEREST", "AFRICAN_AMERICAN");
        preferences.put("NUM_DISTRICTS", "6");
        preferences.put("MAJMIN_UP", "50");
        preferences.put("MAJMIN_LOW", "5");
        preferences.put("POPULATION_EQUALITY", "50");
        preferences.put("COMPETITIVENESS", "50");
        preferences.put("EFFICIENCY_GAP", "50");
        preferences.put("COMPACTNESS", "50");
        preferences.put("LENGTH_WIDTH", "50");
        return preferences;
    }

    private void test() {
        HashMap<String, String> preferences = initPreferences();
        int i;
        for (i = 0; i < 10; i++){
            Algorithm algorithm = new Algorithm(preferences, new State());
            algorithm.graphPartition();
            State state = algorithm.getState();
            StateManager.getInstance().save(state);
        }
//        List<District> districts = state.getDistricts();
//        for(int i = 0; i < districts.size(); i++) {
//            System.out.printf("District %d population : %d \n", i, districts.get(i).getData().getDemographic().getPopulation(RaceType.ALL));
//        }
    }

//    private Collection<SummaryObject> constructBatchSummaryObjects(){
//        State s1 = StateManager.getInstance().findById(new Long(4423));
//        State s2 = StateManager.getInstance().findById(new Long(4442));
//        State s3 = StateManager.getInstance().findById(new Long(4453));
//        State s4 = StateManager.getInstance().findById(new Long(4462));
//        State s5 = StateManager.getInstance().findById(new Long(4472));
//        State s6 = StateManager.getInstance().findById(new Long(4482));
//        State s7 = StateManager.getInstance().findById(new Long(4492));
//        State s8 = StateManager.getInstance().findById(new Long(4502));
//        State s9 = StateManager.getInstance().findById(new Long(4512));
//        State s10 = StateManager.getInstance().findById(new Long(4522));
//
//        return null;
//    }

//    private void initData(){
//
//        Administrator administrator = new Administrator("admin@gmail.com", "cse308");
//        administratorRepository.save(administrator);
//    }
}
