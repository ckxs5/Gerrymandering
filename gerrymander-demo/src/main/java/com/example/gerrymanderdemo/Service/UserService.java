package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.UserRepository;
import com.example.gerrymanderdemo.model.Enum.UserType;
import com.example.gerrymanderdemo.model.Exception.PasswordIncorrectException;
import com.example.gerrymanderdemo.model.Exception.UserExistException;
import com.example.gerrymanderdemo.model.Exception.UserNotFoundException;
import com.example.gerrymanderdemo.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User find(String userEmail, String password) throws UserNotFoundException, PasswordIncorrectException {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isPresent()) {
            if (BCrypt.checkpw(password, user.get().getPassword())) {
                return user.get();
            } else {
                throw new PasswordIncorrectException();
            }
        } else {
            throw new UserNotFoundException();
        }
    }

    public User addUser(User user) throws UserExistException{
        String orginPassword = user.getPassword();
        String hashedPassword = BCrypt.hashpw(orginPassword, BCrypt.gensalt());
        user.setUserType(UserType.USER);
        user.setPassword(hashedPassword);
        if (userRepository.findByEmail(user.getEmail()).isPresent() || orginPassword.equals("")) {
            throw new UserExistException();
        } else {
            return userRepository.save(user);
        }
    }

    public List<User> findAll() {
        return (List) userRepository.findAll();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User findById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(UserNotFoundException::new);
    }
}
