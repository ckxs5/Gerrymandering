package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.RaceType;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.Iterator;

public class ClusterManager {
    Collection<Cluster> clusters;
    Collection<Pair> clusterPairs;


    //TODO: return type,argument insistent with class diagram
    public Collection<Cluster> filterClusters(int idealClusterPop){ //TODO: parameter is from state object
        Collection<Cluster> filteredClusters =  new Collection<Cluster>();
        Iterator<Cluster> iterator = clusters.iterator();

        while (iterator.hasNext()) {
            Cluster clusterElement = iterator.next();
            int totalPop = clusterElement.getData().getDemographic().getPopulationByRace(RaceType.ALL);
            if (totalPop <= idealClusterPop)
            filteredClusters.add(clusterElement);
        }

        return filteredClusters;
    }

    //TODO
    public void addClusterPairs(Collection<Cluster> filteredClusters){
        if (!filteredClusters.isEmpty()) {
            Pair bestPair = filteredClusters.iterator().next().getBestPair();
            clusterPairs.add(bestPair);
            removePairFromCandidates(filteredClusters,bestPair);
            addClusterPairs(filteredClusters);
        }
    }

    public void removePairFromCandidates(Collection<Cluster> filteredClusters, Pair clusterPair){
        Cluster c1 = clusterPair.getElement1();
        Cluster c2 = clusterPair.getElement2();
        filteredClusters.remove(c1);
        filteredClusters.remove(c2);
    }

    //TODO: parameter inconsistent with class diagram
    public void combineClusters(){
        Iterator<Pair> iterator = clusterPairs.iterator();

        while (iterator.hasNext()) {
            Pair pair = iterator.next();
            Cluster c1 = pair.getElement1();
            Cluster c2 = pair.getElement2();
            Cluster newCluster = new Cluster(c1,c2);
            removeClusterFromCollection(c1);
            removeClusterFromCollection(c2);
            addClusterToCollection(newCluster);
        }
    }

    public void removeClusterFromCollection(Cluster removedCluster){
        if(clusters.contains(removedCluster)) {
            clusters.remove(removedCluster);
        }
    }

    public void addClusterToCollection(Cluster newCluster){
        clusters.add(newCluster);
    }



}
