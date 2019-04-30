package com.example.gerrymanderdemo.Bootstrap;

import com.example.gerrymanderdemo.Repository.AdministratorRepository;
import com.example.gerrymanderdemo.model.ClusterManager;
import com.example.gerrymanderdemo.model.User.Administrator;
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
        //TODO
        ClusterManager cm = new ClusterManager();
        cm.test();
    }

    private void initData(){

        Administrator administrator = new Administrator("admin@gmail.com", "cse308");
        administratorRepository.save(administrator);
    }
}
