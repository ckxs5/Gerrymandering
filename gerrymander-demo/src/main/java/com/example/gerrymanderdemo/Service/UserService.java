package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.UserRepository;
import com.example.gerrymanderdemo.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User find(String userEmail, String password) {
        return userRepository.findByEmailAndPassword(userEmail, password);
    }

    public User addUser(User user) {
        System.out.println(user);
       User add = userRepository.findByEmail(user.getEmail());
       System.out.println(add);
       if (add != null) {
           return null;
       }
       else {
           return userRepository.save(user);
       }
    }
}
