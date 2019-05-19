package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.Service.*;

public class DistrictManager {
    private static DistrictManager single_instance;
    private static DistrictService districtService;
    private static DataService dataService;
    private static  VoteService voteService;
    private static DemographicService demographicService;
    private static BoundaryService boundaryService;



    private DistrictManager(DistrictService districtService, DataService dataService, DemographicService demographicService, VoteService voteService, BoundaryService boundaryService) {
        DistrictManager.districtService = districtService;
        DistrictManager.dataService = dataService;
        DistrictManager.voteService = voteService;
        DistrictManager.demographicService = demographicService;
        DistrictManager.boundaryService = boundaryService;
    }

    public static void setInstance (DistrictService districtService, DataService dataService, DemographicService demographicService, VoteService voteService, BoundaryService boundaryService) {
        single_instance = new DistrictManager(districtService, dataService, demographicService, voteService, boundaryService);
    }

    public static DistrictManager getInstance() {
        return single_instance;
    }

    public District save(District district) {
        district.getData().setBoundary(boundaryService.save(district.getData().getBoundary()));
        district.getData().setVoteData(voteService.save(district.getData().getVoteData()));
        district.getData().setDemographic(demographicService.save(district.getData().getDemographic()));
        district.setData(dataService.save(district.getData()));
        return districtService.save(district);
    }

    public District findById(Long id) {
        return districtService.findById(id);
    }
}
