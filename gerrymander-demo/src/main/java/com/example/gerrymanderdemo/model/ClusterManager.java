package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Enum.RaceType;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class ClusterManager {
    Collection<Cluster> clusters;
    Collection<Pair<Cluster>> clusterPairs;

    public ClusterManager(Collection<Cluster> clusters) {
        this.clusters = clusters;
    }

    //TODO: return type,argument insistent with class diagram
    public Collection<Cluster> filterClusters(int idealClusterPop){ //TODO: parameter is from state object
        Collection<Cluster> filteredClusters = new ArrayList<>();
        Iterator<Cluster> iterator = clusters.iterator();

        while (iterator.hasNext()) {
            Cluster clusterElement = iterator.next();
            int totalPop = clusterElement.getData().getDemographic().getPopulation(RaceType.ALL);
            if (totalPop <= idealClusterPop)
            filteredClusters.add(clusterElement);
        }

        return filteredClusters;
    }

    //TODO
    private void addClusterPairs(Collection<Cluster> filteredClusters){
        if (!filteredClusters.isEmpty()) {
            Pair bestPair = filteredClusters.iterator().next().getBestPair();
            clusterPairs.add(bestPair);
            removePairFromCandidates(filteredClusters,bestPair);
            addClusterPairs(filteredClusters);
        }
    }

    private void removePairFromCandidates(Collection<Cluster> filteredClusters, Pair<Cluster> clusterPair){
        Cluster c1 = clusterPair.getElement1();
        Cluster c2 = clusterPair.getElement2();
        filteredClusters.remove(c1);
        filteredClusters.remove(c2);
    }

    //TODO: parameter inconsistent with class diagram
    public void combineClusters(){
        Iterator<Pair<Cluster>> iterator = clusterPairs.iterator();

        while (iterator.hasNext()) {
            Pair<Cluster> pair = iterator.next();
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
