package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Arrays;

@Entity
public class Vote implements ResponseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @OrderColumn
    private int[] votes;

    public Vote() {
        votes = new int[Party.values().length];
    }

    public Vote(Vote vote) {
        this();
        for(Party p : Party.values()){
            votes[p.ordinal()] = vote.getVote(p);
        }
    }

    public Vote(Vote v1, Vote v2){
        this();
        for (Party party: Party.values()) {
            setVote(party, v1.getVote(party) + v2.getVote(party));
        }
    }

    public Vote(int[] votes) {
        if (votes.length != Party.values().length)
            throw new IndexOutOfBoundsException("Not enough elements in votes, at least " + Party.values().length);
        this.votes = votes;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int[] getVotes() {
        return votes;
    }

    public void setVotes(int[] votes) {
        this.votes = votes;
    }

    public void setVote(Party party, int vote) { votes[party.ordinal()] = vote; }

    public int getVote(Party party) {
        return votes[party.ordinal()];
    }

    public double getPercent (Party party) {
        return votes[party.ordinal()] * 1.0 / totalVotes();
    }

    public int totalVotes() {
        return Arrays.stream(votes).reduce(Integer::sum).orElse(0);
    }

    public Party getResult() {
        return votes[Party.DEMOCRATIC.ordinal()] > votes[Party.REPUBLICAN.ordinal()] ? Party.DEMOCRATIC : Party.REPUBLICAN;
    }

    public void add(Vote other) {
        for(Party p : Party.values()){
            votes[p.ordinal()] += other.getVote(p);
        }
    }

    public Vote remove(Vote other){
        for (Party p : Party.values()) {
//            if(other.getVote(p) > votes[p.ordinal()]) {
//                throw new IllegalArgumentException();
//            }
            votes[p.ordinal()] -= other.getVote(p);
        }
        return other;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            for (Party party : Party.values()) {
                json.put(party.toString(), votes[party.ordinal()]);
            }
        }catch (JSONException ex) {
            System.out.println("Unexpected error occurs when converting a Vote object to JSON object");
            return null;
        }
        return json;
    }
}
