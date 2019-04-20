package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Repository.AdministratorRepository;
import com.example.gerrymanderdemo.model.Administrator;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private AdministratorRepository administratorRepository;

    public DevBootstrap(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData(){

        Administrator administrator = new Administrator("testAdmin", "admin@gmail.com", "cse308");
        administratorRepository.save(administrator);
    }
}
