package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.DemographicRepository;
import com.example.gerrymanderdemo.model.Data.Demographic;
import org.springframework.stereotype.Service;

@Service
public class DemographicService {
    DemographicRepository demographicRepository;

    public DemographicService(DemographicRepository demographicRepository) {
        this.demographicRepository = demographicRepository;
    }

    public Demographic save(Demographic demographic) {
        return demographicRepository.save(demographic);
    }
}
