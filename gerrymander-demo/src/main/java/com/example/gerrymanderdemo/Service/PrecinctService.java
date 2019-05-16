package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.PrecinctRepository;
import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.model.Precinct;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Precinct> findById(Long id) {
        return precinctRepository.findById(id);
    }

    public List<Precinct> findAllByState(StateName state) { return precinctRepository.findAllByState(state); }
}
