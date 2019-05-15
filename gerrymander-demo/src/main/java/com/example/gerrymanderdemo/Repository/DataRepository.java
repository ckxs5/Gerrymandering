package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Data.Data;
import org.springframework.data.repository.CrudRepository;

public interface DataRepository extends CrudRepository<Data, String> {
}
