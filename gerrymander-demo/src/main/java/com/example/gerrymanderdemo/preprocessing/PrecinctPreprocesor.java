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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
//        precincts = constructNeighbours();
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
        System.out.println("Start Constructing Precincts");
        List<Precinct> precincts = new ArrayList<>();
        precinctsData.forEach(x -> {
            Precinct precinct = x.toIdPrecinct();
//            Optional<Precinct> found = precinctService.findById(precinct.getId());
//            if (found.isPresent()){
//                precincts.add(precinct);
//                return;
//            }
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
            precinct.setData(data);
            precinct = precinctService.save(precinct);
            precincts.add(precinct);

        });
        System.out.println("End Constructing Precincts");
        return precincts;
    }

//    private List<Precinct> constructNeighbours() {
//        System.out.println("Start Construct Neighbours");
//        int count = 0;
//        for (PrecinctConstructor pc : precinctsData) {
//            System.out.printf("%d / %d\n", precinctsData.size(), ++count);
//            Precinct target = getSavedPrecinctById(pc.getId());
//            if (target == null) {
//                System.out.printf("Error happen when constructing precinct: %s !!!!!!\n", pc.getId());
//                break;
//            }
//            HashSet<Precinct> ns = new HashSet<>();
//            for (Long neighbourId : pc.getNeighbors()) {
//                ns.add(getSavedPrecinctById(neighbourId));
//            }
//            target.setNeighbors(ns);
//            target = precinctService.save(target);
//        }
//        System.out.println("End Construct Neighbours");
//        return precincts;
//    }

    private Precinct getSavedPrecinctById(Long id) {
        for (Precinct p: precincts) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
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

