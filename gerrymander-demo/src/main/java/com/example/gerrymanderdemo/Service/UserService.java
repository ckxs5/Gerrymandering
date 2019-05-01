package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.PasswordEncoder.PasswordEncoder;
import com.example.gerrymanderdemo.Repository.UserRepository;
import com.example.gerrymanderdemo.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User find(String userEmail, String password) {
        return userRepository.findByEmailAndPassword(userEmail, password);
    }

    public User find(String userType){
        return userRepository.findByUserType(userType);
    }

    public User addUser(User user) {
        System.out.println("User: "+user);
        User add = userRepository.findByEmail(user.getEmail());
        System.out.println("Add: "+ add);
        if (add != null) {
            return null;
        }else{
           return userRepository.save(user);
       }
    }
}
