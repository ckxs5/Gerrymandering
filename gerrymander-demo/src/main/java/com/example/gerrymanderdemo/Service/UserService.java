package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.AdministratorRepository;
import com.example.gerrymanderdemo.Repository.GuestRepository;
import com.example.gerrymanderdemo.model.Guest;
import com.example.gerrymanderdemo.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private AdministratorRepository administratorRepository;
    private GuestRepository guestRepository;

    public UserService(AdministratorRepository administratorRepository, GuestRepository guestRepository) {
        this.administratorRepository = administratorRepository;
        this.guestRepository = guestRepository;
    }

    public User find(String userEmail, String password) {
        User user = administratorRepository.findByEmailAndPassword(userEmail, password);
        if (user == null)
            user = guestRepository.findByEmailAndPassword(userEmail, password);
        return user;
    }

    public Guest addGuest(Guest guest) {
       Guest add = guestRepository.findByEmail(guest.getEmail());
       if (add != null) {
           return null;
       }
       else {
           return guestRepository.save(guest);
       }
    }
}
