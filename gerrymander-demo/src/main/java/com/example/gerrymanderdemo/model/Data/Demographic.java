package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Enum.RaceType;

public class Demographic {
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


}
