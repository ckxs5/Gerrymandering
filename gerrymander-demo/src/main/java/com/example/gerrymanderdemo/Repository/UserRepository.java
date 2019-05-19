package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.User.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserType(String userType);
    void deleteByEmail(String email);
}
