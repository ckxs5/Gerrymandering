package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
public class Demographic implements ResponseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @OrderColumn
    private int[] population = new int[RaceType.values().length];

    public Demographic() {
    }

    public Demographic(Demographic demographic) {
        demographic.add(demographic);
    }

    public Demographic(Demographic d1, Demographic d2) {
        for (RaceType raceType: RaceType.values()) {
            setPopulation(raceType, d1.getPopulation(raceType) + d2.getPopulation(raceType));
        }
    }

    public Demographic(int[] population) {
        if (population.length != RaceType.values().length) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(population, 0, this.population, 0, population.length);

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int[] getPopulation() {
        return population;
    }

    public void setPopulation(int[] population) {
        System.arraycopy(population, 0, this.population, 0, population.length);
    }

    public void setPopulation(RaceType raceType, int pop) { population[raceType.ordinal()] = pop; }

    public int getPopulation(RaceType race) {
        return population[race.ordinal()];
    }

    public int totalPopulation() {
        return population[RaceType.ALL.ordinal()];
    }

    public double getPercentByRace(RaceType race) {
        try{
            return (1.0 * population[race.ordinal()] / population[RaceType.ALL.ordinal()]);
        } catch (ArithmeticException ex) {
            return 0;
        }
    }

    public void add(Demographic other) {
        for(RaceType r : RaceType.values()){
            population[r.ordinal()] += other.getPopulation(r);
        }
    }

    public Demographic remove(Demographic other) {
        for(RaceType r : RaceType.values()){
            population[r.ordinal()] -= other.getPopulation(r);
        }
        return other;
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
