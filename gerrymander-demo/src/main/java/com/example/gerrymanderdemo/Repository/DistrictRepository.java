package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.District;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DistrictRepository extends CrudRepository<District, Long> {
    Optional<District> findById(Long id);
}
