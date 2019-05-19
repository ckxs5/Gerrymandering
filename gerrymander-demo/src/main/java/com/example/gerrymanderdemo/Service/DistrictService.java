package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.DistrictRepository;
import com.example.gerrymanderdemo.model.District;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {
    @Autowired
    private DistrictRepository districtRepository;

    public District save(District district) {
        System.out.println(district.getId());
        return districtRepository.save(district);
    }

    public District findById(Long id) {
        return districtRepository.findById(id).orElse(null);
    }
}
