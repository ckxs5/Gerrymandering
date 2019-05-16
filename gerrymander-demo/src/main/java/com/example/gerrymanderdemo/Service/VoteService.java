package com.example.gerrymanderdemo.Service;

import com.example.gerrymanderdemo.Repository.VoteRepository;
import com.example.gerrymanderdemo.model.Data.Vote;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
    VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public Vote save(Vote vote) {
        return voteRepository.save(vote);
    }
}
