package com.example.gerrymanderdemo.model;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.example.gerrymanderdemo.model.Enum.StateName;

import java.util.*;
import java.util.stream.Collectors;

public class ClusterManager {
    private List<Cluster> clusters;
    private int targetNumCluster;
    private RaceType communityOfInterest;
    private int totalPopulation = 0;
    // TODO : Should it be here?
    private List<District> districts;
    private int targetPopulation;

    public ClusterManager(RaceType communityOfInterest, int targetNumCluster, List<Precinct> precincts) {
        this.targetNumCluster = targetNumCluster;
        this.communityOfInterest = communityOfInterest;
        clusters = new ArrayList<>();
        for (Precinct p : precincts) {
            clusters.add(new Cluster(p));
            totalPopulation += p.getData().getDemographic().getPopulation(RaceType.ALL);
        }
        this.targetPopulation = totalPopulation / targetNumCluster;
        System.out.printf("Construct %d clusters at the beginning\n", clusters.size());
        constructEdges();
    }

    private void constructEdges() {
        List<Cluster> done = new ArrayList<>();
        for (Cluster c : clusters) {
            for (Precinct p : c.getPrecinct().getNeighbors()) {
                Cluster nei = findClusterByPrecinct(p);
                if (! done.contains(nei)) {
                    Edge edge = new Edge(c, nei, communityOfInterest, targetPopulation);
                    c.addEdge(edge);
                    try {
                        nei.addEdge(edge);
                    } catch (NullPointerException ex) {
                        nei = new Cluster(p);
                        nei.addEdge(edge);
                    }
                }
            }
            done.add(c);
        }
    }

    private Cluster findClusterByPrecinct(Precinct precinct) {
        for (Cluster c : clusters) {
            if (c.getPrecinct().equals(precinct)) {
                return c;
            }
        }
        return null;
    }

    public void run() {
        boolean done = false;
        int count = 0;
        while (!done) {
            System.out.printf("Partition Run : %d \n", ++count);
            done = !runOnce();
        }
    }

    public boolean runOnce() {
        if (clusters.size() <= targetNumCluster) {
            return false;
        }
        boolean combined = false;
        List<Cluster> candidates = filterClusters();
        int count = 0;
        while (candidates.size() > 1 && count < candidates.size() - 1) {
            Cluster target = candidates.get(count);
            Edge edge = target.getBestEdge();
            Cluster other = edge.getTheOther(target);

            if (candidates.contains(other)) {
                merge(edge);
                candidates.remove(target);
                candidates.remove(other);
                combined = true;
                count = 0;
            }
            count++;
        }
        return combined;
    }

    private List<Cluster> filterClusters(){
        return clusters.stream()
                .filter(cluster -> cluster.getData().getDemographic().getPopulation(RaceType.ALL) < targetPopulation)
                .collect(Collectors.toList());
    }

    private void merge(Pair<Cluster> clusterPair) {
        clusters.remove(clusterPair.getElement1());
        clusters.remove(clusterPair.getElement2());
        clusters.add(new Cluster(clusterPair.getElement1(), clusterPair.getElement2()));
    }

    public List<District> balancePopulation() {
        District from = getDistrictWithHighestPop();
        if (from.getData().getDemographic().getPopulation(RaceType.ALL) < targetPopulation) {
            return districts;
        }

        District to = from.getLowestPopNeigbour();
        while (from.getData().getDemographic().getPopulation(RaceType.ALL) > targetPopulation
            || to.getData().getDemographic().getPopulation(RaceType.ALL) < targetPopulation) {
            if(!from.passPrecinct(to)) {
                return districts;
            }
        }
        return districts;
    }

    private District getDistrictWithHighestPop() {
        District target = districts.get(0);
        for(District d : districts) {
            if(d.getData().getDemographic().getPopulation(RaceType.ALL) > target.getData().getDemographic().getPopulation(RaceType.ALL)) {
                target = d;
            }
        }
        return target;
    }

    public List<District> toDistricts() {
        List<District> districts = new ArrayList<>();
        for (Cluster c : clusters) {
            districts.add(c.toDistrict());
        }

        for (Precinct p : PrecinctManager.getPrecincts(StateName.MINNESOTA).values()) {
            p.setDistrict(p.getDistrict(), true);
        }
        this.districts = districts;
        return districts;
    }


}
