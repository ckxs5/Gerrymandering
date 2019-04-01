package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.AdministratorRepository;
import com.example.gerrymanderdemo.Repository.GuestRepository;
import com.example.gerrymanderdemo.Repository.UserRepository;
import com.example.gerrymanderdemo.model.Guest;
import com.example.gerrymanderdemo.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private AdministratorRepository administratorRepository;
    private GuestRepository guestRepository;
    private UserRepository userRepository;

    public UserService(AdministratorRepository administratorRepository, GuestRepository guestRepository, UserRepository userRepository) {
        this.administratorRepository = administratorRepository;
        this.guestRepository = guestRepository;
        this.userRepository = userRepository;
    }

    public User find(String userEmail, String password) {
        return userRepository.findByEmailAndPassword(userEmail, password);
    }

    public User addGuest(User user) {
       User add = userRepository.findByNameOrEmail(user.getName(), user.getEmail());
       if (add != null) {
           return null;
       }
       else {
           return userRepository.save(user);
       }
    }
}
