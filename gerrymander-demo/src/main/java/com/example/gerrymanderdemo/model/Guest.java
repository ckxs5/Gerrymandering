package com.example.gerrymanderdemo.model;

import javax.persistence.Entity;

@Entity
public class Guest extends User {

    public Guest(String name, String email, String password) {
        super(name, email, password);
    }
}
