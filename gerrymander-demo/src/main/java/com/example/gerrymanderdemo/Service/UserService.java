package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.AdministratorRepository;
import com.example.gerrymanderdemo.Repository.UserRepository;
import com.example.gerrymanderdemo.model.User.User;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private AdministratorRepository administratorRepository;
    private UserRepository userRepository;

    public UserService(AdministratorRepository administratorRepository, UserRepository userRepository) {
        this.administratorRepository = administratorRepository;
        this.userRepository = userRepository;
    }

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
