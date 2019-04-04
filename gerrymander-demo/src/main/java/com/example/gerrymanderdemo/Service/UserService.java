package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.UserRepository;
import com.example.gerrymanderdemo.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService (UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User find(String userEmail, String password) {
        return userRepository.findByEmailAndPassword(userEmail, password);
    }

    public User addUser(User user) {
       User add = userRepository.findByNameOrEmail(user.getName(), user.getEmail());
       if (add != null) {
           return null;
       }
       else {
           return userRepository.save(user);
       }
    }
}
