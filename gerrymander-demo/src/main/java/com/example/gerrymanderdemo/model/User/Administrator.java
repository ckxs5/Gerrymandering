package com.example.gerrymanderdemo.model.User;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Administrator extends User {

    @OneToMany
    Collection<User> userList;

    public Administrator() {
    }

    public Administrator(String email, String password) {
        super(email, password);
        this.userList = new ArrayList<>();
    }

    public Administrator(String email, String password, Collection<User> userList) {
        super(email, password);
        this.userList = userList;
    }

    public Collection<User> getUserList() {
        return userList;
    }

    public void setUserList(Collection<User> userList) {
        this.userList = userList;
    }

    public void addToList(User user) {
        userList.add(user);
    }

    public User removeFromList(User user) {
        if (userList.remove(user))
            return user;
        else
            return null;
    }

//    Todo: EditUser (What to do?)


    @Override
    public String toString() {
        String str = super.toString();
        str = str.substring(0, str.length()-1);
        return str + "\nAdministrator{" +
                "userList=" + userList +
                "}\n}";
    }
}
