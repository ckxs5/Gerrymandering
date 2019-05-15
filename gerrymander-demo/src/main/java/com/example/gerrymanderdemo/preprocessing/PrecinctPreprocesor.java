package com.example.gerrymanderdemo.preprocessing;

import com.example.gerrymanderdemo.Service.*;
import com.example.gerrymanderdemo.model.Data.Boundary;
import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Data.Demographic;
import com.example.gerrymanderdemo.model.Data.Vote;
import com.example.gerrymanderdemo.model.Precinct;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrecinctPreprocesor {
    private List<PrecinctConstructor> precinctsData;
    private PrecinctService precinctService;
    private DemographicService demographicService;
    private VoteService voteService;
    private BoundaryService boundaryService;
    private DataService dataService;
    private List<Precinct> precincts;

    public PrecinctPreprocesor(PrecinctService precinctService, DemographicService demographicService,
                               VoteService voteService, BoundaryService boundaryService, DataService dataService, String filename) {
        this.precinctService = precinctService;
        this.demographicService = demographicService;
        this.voteService = voteService;
        this.boundaryService = boundaryService;
        this.dataService = dataService;
        prepareData(filename);
        checkData();
        precincts = getIdPrecincts();
    }

    private void prepareData(String filename) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            File file = new File(filename);
            precinctsData = mapper.readValue(file, new TypeReference<List<PrecinctConstructor>>(){});
        } catch (IOException e2) {
            System.out.printf("Error when reading line from file %s\n", filename);
            e2.printStackTrace();
        }
    }

    private List<Precinct> getIdPrecincts(){
        List<Precinct> precincts = new ArrayList<>();
        precinctsData.forEach(x -> {
            Precinct precinct = x.toIdPrecinct();
            Demographic demographic = x.toDemographic();
            demographic = demographicService.save(demographic);
            Vote vote = x.toVote();
            vote = voteService.save(vote);
            Boundary boundary = x.toBoundary();
            boundary = boundaryService.save(boundary);
            Data data = new Data();
            data.setVoteData(vote);
            data.setBoundary(boundary);
            data.setDemographic(demographic);
            data = dataService.save(data);
            precincts.add(precinct);
            precinct.setData(data);
            precinctService.save(precinct);
        });
        return precincts;
    }


    private void checkData() {
        System.out.println(precinctsData.get(0));
        System.out.println(precinctsData.size());
    }

    private Vote prepareVote (int n) {
        return null;
    }

    private Demographic prepareDemograpgic (int n) {
        return null;
    }

    private Boundary prepareBoundary (int n) {
        return  null;
    }
}

