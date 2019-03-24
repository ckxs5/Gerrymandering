package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Long> {
    Guest findByNameAndPassword(String name, String password);
    Guest findByEmailAndPassword(String email, String passworc);
    Guest findByName(String name);
    Guest findByEmail(String email);
}
