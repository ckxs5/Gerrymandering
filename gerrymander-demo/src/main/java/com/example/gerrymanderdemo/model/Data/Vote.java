package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Enum.Party;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Vote implements ResponseObject {
    int[] votes;

    public Vote(){}

    public Vote(int[] votes) {
        if (votes.length != Party.values().length)
            throw new IndexOutOfBoundsException("Not enough elements in votes, at least " + Party.values().length);
        this.votes = votes;

    }

    public int[] getVotes() {
        return votes;
    }

    public void setVotes(int[] votes) {
        this.votes = votes;
    }

    public int getVote(Party party) {
        return votes[party.ordinal()];
    }

    public double getPercent (Party party) {
        return votes[party.ordinal()] * 1.0 / totalVotes();
    }

    public int totalVotes() {
        return Arrays.stream(votes).reduce(Integer::sum).orElse(0);
    }

    public String toJSON() {
        StringBuffer json = new StringBuffer();
        json.append("{");
        for (Party party : Party.values())
            json.append(String.format("%s: %d,", party.toString(), votes[party.ordinal()]));

        // Delete the last COMMA
        json.deleteCharAt(json.length() - 1);
        json.append("}");
        return json.toString();
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            for (Party party : Party.values())
                json.put(party.toString(), votes[party.ordinal()]);
        }catch (JSONException ex) {
            System.out.println("Unexpected error occurs when converting a Vote object to JSON object");
            return null;
        }
        return json;
    }
}
