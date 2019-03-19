package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Long> {
}
