package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.DataRepository;
import com.example.gerrymanderdemo.model.Data.Data;
import org.springframework.stereotype.Service;

@Service
public class DataService {
    DataRepository dataRepository;

    public DataService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Data save(Data data) {
        return dataRepository.save(data);
    }
}
