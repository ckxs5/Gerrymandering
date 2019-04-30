package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.RaceType;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Iterator;

public class ClusterManager {
    Collection<Cluster> clusters;
    Collection<Cluster> filteredClusters;
    Collection<Pair> clusterPairs;



    public Collection<Cluster> filterClusters(int idealClusterPop){ //TODO: parameter is from state object
        Iterator<Cluster> iterator = clusters.iterator();

        while (iterator.hasNext()) {
            Cluster clusterElement = iterator.next();
            int totalPop = clusterElement.getData().getDemographic().getPopulationByRace(RaceType.ALL);
            if (totalPop <= idealClusterPop)
            filteredClusters.add(clusterElement);
        }
    }

    public void addClusterPairs(){
        Iterator<Cluster> iterator = filteredClusters.iterator();

        while (iterator.hasNext()) {
            Pair bestPair = iterator.next().getBestPair();
            clusterPairs.add(bestPair);
            removePairFromCandidates(bestPair);
        }
    }

    public void removePairFromCandidates(Pair clusterPair){
        Cluster c1 = clusterPair.getElement1();
        Cluster c2 = clusterPair.getElement2();
        filteredClusters.remove(c1);
        filteredClusters.remove(c2);
    }



}
