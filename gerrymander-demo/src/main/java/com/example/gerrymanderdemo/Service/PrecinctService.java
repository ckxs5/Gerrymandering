package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.PrecinctRepository;
import com.example.gerrymanderdemo.model.Precinct;
import org.springframework.stereotype.Service;

@Service
public class PrecinctService {

    private PrecinctRepository precinctRepository;

    public PrecinctService(PrecinctRepository precinctRepository) {
        this.precinctRepository = precinctRepository;
    }

    public Precinct save(Precinct precinct) {
        return precinctRepository.save(precinct);
    }
}
