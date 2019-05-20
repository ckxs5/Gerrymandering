package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.StateRepository;
import com.example.gerrymanderdemo.model.District;
import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.model.State;
import org.springframework.stereotype.Service;

@Service
public class StateService {

    private StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }
    //TODO
    public State saveState(State state) {
        return stateRepository.save(state);
    }
//    private StateRepository stateRepository;
//
//    final static String orgId = "00000";
//
//    public StateService(StateRepository stateRepository) {
//        this.stateRepository = stateRepository;
//    }
//
//    public State findById(String id) {
//        return stateRepository.findById(id).orElse(null);
//    }
    //TODO
    public State getOriginState(StateName name){ return null;}

    public State findById(Long id) {
        return stateRepository.findById(id).orElse(null);
    }
}
