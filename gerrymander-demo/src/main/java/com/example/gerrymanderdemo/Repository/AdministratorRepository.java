package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Administrator;
import org.springframework.data.repository.CrudRepository;

public interface AdministratorRepository extends CrudRepository<Administrator, Long> {
    Administrator findByNameAndPassword(String name, String password);
    Administrator findByEmailAndPassword(String email, String passworc);
}
