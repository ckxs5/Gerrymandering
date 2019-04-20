package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Repository.AdministratorRepository;
import com.example.gerrymanderdemo.Service.StateService;
import com.example.gerrymanderdemo.model.Administrator;
import com.example.gerrymanderdemo.model.Data.Vote;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private AdministratorRepository administratorRepository;

    public DevBootstrap(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }

    private void initData(){

        Administrator administrator = new Administrator("testAdmin", "admin@gmail.com", "cse308");
        administratorRepository.save(administrator);
    }
}
