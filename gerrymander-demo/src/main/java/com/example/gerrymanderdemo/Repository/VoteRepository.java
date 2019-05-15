package com.example.gerrymanderdemo.Repository;

import com.example.gerrymanderdemo.model.Data.Vote;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, String> {
}
