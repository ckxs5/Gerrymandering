package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Demographic implements ResponseObject {
    @Id
    String id;
    int[] population;

    public Demographic(int[] population) {
        if (population.length != RaceType.values().length)
            throw new IndexOutOfBoundsException();
        this.population = population;
    }

    public int[] getPopulation() {
        return population;
    }

    public void setPopulation(int[] population) {
        this.population = population;
    }

    public int getPopulationByRace(RaceType race) {
        return population[race.ordinal()];
    }

    public int totalPopulation() {
        return population[RaceType.ALL.ordinal()];
    }

    public double getPercentByRace(RaceType race) {
        return (double)(population[race.ordinal()] / population[RaceType.ALL.ordinal()]);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        try {
            for (RaceType race : RaceType.values())
                json.put(race.toString(), population[race.ordinal()]);
        }catch (JSONException ex) {
            System.out.println("Unexpected error occurs when converting a Vote object to JSON object");
            return null;
        }
        return json;
    }
}
