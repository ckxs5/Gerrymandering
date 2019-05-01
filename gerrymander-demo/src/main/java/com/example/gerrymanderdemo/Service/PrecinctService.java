package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.PrecinctRepository;
import com.example.gerrymanderdemo.model.Precinct;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecinctService {

    private PrecinctRepository precinctRepository;

    public PrecinctService(PrecinctRepository precinctRepository) {
        this.precinctRepository = precinctRepository;
    }

    public Precinct save(Precinct precinct) {
        return precinctRepository.save(precinct);
    }

    public List<Precinct> findAll() {
        return Lists.newArrayList(precinctRepository.findAll());
    }
}
