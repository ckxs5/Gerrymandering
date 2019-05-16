package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Data.Demographic;
import org.springframework.data.repository.CrudRepository;

public interface DemographicRepository extends CrudRepository<Demographic, String> {
}
