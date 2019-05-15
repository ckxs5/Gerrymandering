package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.State;
import org.springframework.data.repository.CrudRepository;

public interface StateRepository extends CrudRepository<State, Long> {
    State findStateById(Long originStateId);
}
