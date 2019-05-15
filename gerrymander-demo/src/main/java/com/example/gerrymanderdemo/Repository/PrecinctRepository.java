package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Precinct;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface PrecinctRepository extends CrudRepository<Precinct, Long> {
    Optional<Precinct> findById(Long id);
}
