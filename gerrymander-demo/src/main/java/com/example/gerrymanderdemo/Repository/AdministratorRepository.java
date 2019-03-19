package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Administrator;
import org.springframework.data.repository.CrudRepository;

public interface AdministratorRepository extends CrudRepository<Administrator, Long> {
}
