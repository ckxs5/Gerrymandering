package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Enum.Party;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VoteTest {
    private int[] testVoteNums = new int[Party.values().length];
    private int sum;

    @Before
    public void prepare(){
        for (int i = 0; i < testVoteNums.length; i++) {
            testVoteNums[i] = (int)(Math.random() * 1000 + 100);
            sum += testVoteNums[i];
        }
    }

    @Test
    public void getVote() {
        Vote vote = new Vote(testVoteNums);
        for (Party party : Party.values())
            assertEquals(vote.getVote(party), testVoteNums[party.ordinal()]);
    }

    @Test
    public void totalVotes() {
        Vote vote = new Vote((testVoteNums));
        assertEquals(vote.totalVotes(), sum);
    }

    @Test
    public void getPercent() {
        Vote vote = new Vote((testVoteNums));
        for (Party party : Party.values())
            assertEquals(vote.getPercent(party), (double)(testVoteNums[party.ordinal()] / sum), 5);
    }


}