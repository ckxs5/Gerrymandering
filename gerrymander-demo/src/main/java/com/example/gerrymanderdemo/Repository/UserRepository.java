package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    User findByNameOrEmail(String name, String email);
}
