package com.example.gerrymanderdemo.model;

import javax.persistence.Entity;

@Entity
public class Guest extends User {

    public Guest(String name, String password) {
        super(name, password);
    }


}
