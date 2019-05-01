package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.PasswordEncoder.PasswordEncoder;
import com.example.gerrymanderdemo.Repository.UserRepository;
import com.example.gerrymanderdemo.model.Exception.PasswordIncorrectException;
import com.example.gerrymanderdemo.model.Exception.UserExistException;
import com.example.gerrymanderdemo.model.Exception.UserNotFoundException;
import com.example.gerrymanderdemo.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User find(String userEmail, String password) throws UserNotFoundException, PasswordIncorrectException {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            if (user.get().getPassword().equals(password)) {
                return user.get();
            } else {
                throw new PasswordIncorrectException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public User addUser(User user) throws UserExistException{
        System.out.println("User: "+user);
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserExistException();
        } else {
            return userRepository.save(user);
        }
    }
}
