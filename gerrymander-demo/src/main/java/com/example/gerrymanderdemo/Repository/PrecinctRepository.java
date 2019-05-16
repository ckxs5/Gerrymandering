package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Enum.StateName;
import com.example.gerrymanderdemo.model.Precinct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface PrecinctRepository extends CrudRepository<Precinct, Long> {
    Optional<Precinct> findById(Long id);
    List<Precinct> findAllByState(StateName state);
}
