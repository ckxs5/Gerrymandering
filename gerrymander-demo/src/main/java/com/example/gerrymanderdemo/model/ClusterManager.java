package com.example.gerrymanderdemo.model;
import com.example.gerrymanderdemo.Service.PrecinctService;
import com.example.gerrymanderdemo.model.Enum.RaceType;

import java.util.*;
import java.util.stream.Collectors;

public class ClusterManager {
    private List<Cluster> clusters;
    private int targetNumCluster;
    private int totalPopulation = 0;

    public ClusterManager(PrecinctService service, int targetNumCluster) {
        clusters = new ArrayList<>();
        for (Precinct p : service.findAll()) {
            clusters.add(new Cluster(p));
            totalPopulation += p.getData().getDemographic().getPopulation(RaceType.ALL);
        }
        this.targetNumCluster = targetNumCluster;

    }

    public void run() {
        boolean done = false;
        while (!done) {
            done = !runOnce();
        }
    }

    public boolean runOnce() {
        if (clusters.size() <= targetNumCluster)
            return false;

        List<Cluster> candidates = filterClusters();

        Collections.sort(candidates);
        for (int i = 0; i < candidates.size(); i++) {
            Edge edge = candidates.get(i).getBestEdge();
            if (candidates.contains(edge.getTheOther(candidates.get(i)))) {
                merge(edge);
                return true;
            }
        }
        return false;
    }

    private List<Cluster> filterClusters(){
        return clusters.stream()
                .filter(cluster -> cluster.getData().getDemographic().getPopulation(RaceType.ALL)
                        < totalPopulation / targetNumCluster)
                .collect(Collectors.toList());
    }

    private void merge(Pair<Cluster> clusterPair) {
        clusters.remove(clusterPair.getElement1());
        clusters.remove(clusterPair.getElement2());
        clusters.add(new Cluster(clusterPair.getElement1(), clusterPair.getElement2()));
    }

    public Collection<District> toDistricts() {
        Collection<District> districts = new ArrayList<>();
        for (Cluster c : clusters) {
            districts.add(c.toDistrict());
        }
        return districts;
    }


}
