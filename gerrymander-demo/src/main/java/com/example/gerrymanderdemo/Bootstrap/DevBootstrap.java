package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Repository.AdministratorRepository;
import com.example.gerrymanderdemo.Repository.GuestRepository;
import com.example.gerrymanderdemo.model.Administrator;
import com.example.gerrymanderdemo.model.Guest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private AdministratorRepository administratorRepository;
    private GuestRepository guestRepository;

    public DevBootstrap(AdministratorRepository administratorRepository, GuestRepository guestRepository) {
        this.administratorRepository = administratorRepository;
        this.guestRepository = guestRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData(){

        Guest guest = new Guest("testGuest", "test@gmail.com", "cse308");
        Administrator administrator = new Administrator("testAdmin", "admin@gmail.com", "cse308");
        administrator.addToList(guest);
        guestRepository.save(guest);
        administratorRepository.save(administrator);
    }
}
