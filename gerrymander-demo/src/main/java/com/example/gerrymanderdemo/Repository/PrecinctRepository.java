package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Precinct;
import org.springframework.data.repository.CrudRepository;


public interface PrecinctRepository extends CrudRepository<Precinct, String> {
}
