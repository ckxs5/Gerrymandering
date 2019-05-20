package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.*;

public class StateManager {
    private static StateManager single_instance;
    private static StateService stateService;
    private static DataService dataService;
    private static VoteService voteService;
    private static DemographicService demographicService;
    private static BoundaryService boundaryService;



    private StateManager(StateService StateService, DataService dataService, DemographicService demographicService, VoteService voteService, BoundaryService boundaryService) {
        StateManager.stateService = StateService;
        StateManager.dataService = dataService;
        StateManager.voteService = voteService;
        StateManager.demographicService = demographicService;
        StateManager.boundaryService = boundaryService;
    }

    public static void setInstance (StateService StateService, DataService dataService, DemographicService demographicService, VoteService voteService, BoundaryService boundaryService) {
        single_instance = new StateManager(StateService, dataService, demographicService, voteService, boundaryService);
    }

    public static StateManager getInstance() {
        return single_instance;
    }

    public State save(State state) {
        state.getData().setBoundary(boundaryService.save(state.getData().getBoundary()));
        state.getData().setVoteData(voteService.save(state.getData().getVoteData()));
        state.getData().setDemographic(demographicService.save(state.getData().getDemographic()));
        state.setData(dataService.save(state.getData()));
        return stateService.saveState(state);
    }

    public State findById(Long id) {
        return stateService.findById(id);
    }
}
