package com.example.gerrymanderdemo.model;
import com.example.gerrymanderdemo.model.Enum.RaceType;

import java.util.*;
import java.util.stream.Collectors;

public class ClusterManager {
    private List<Cluster> clusters;
    private int targetNumCluster;
    private RaceType communityOfInterest;
    private int totalPopulation = 0;

    public ClusterManager(RaceType communityOfInterest, int targetNumCluster, List<Precinct> precincts) {
        this.targetNumCluster = targetNumCluster;
        this.communityOfInterest = communityOfInterest;
        clusters = new ArrayList<>();
        for (Precinct p : precincts) {
            clusters.add(new Cluster(p));
            totalPopulation += p.getData().getDemographic().getPopulation(RaceType.ALL);
        }
        System.out.printf("Construct %d clusters at the beginning\n", clusters.size());
        constructEdges();
    }

    private void constructEdges() {
        List<Cluster> done = new ArrayList<>();
        for (Cluster c : clusters) {
            for (Precinct p : c.getPrecinct().getNeighbors()) {
                Cluster nei = findClusterByPrecinct(p);
                if (! done.contains(nei)) {
                    Edge edge = new Edge(c, nei, communityOfInterest);
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
        while (!done) {
            done = !runOnce();
        }
    }

    public boolean runOnce() {
        if (clusters.size() <= targetNumCluster) {
            System.out.println(clusters.size());
            return false;
        }

        List<Cluster> candidates = filterClusters();

        Collections.sort(candidates);
        for (int i = 0; i < candidates.size(); i++) {
            Edge edge = candidates.get(i).getBestEdge();
//            //TODO: Delete after test, remove precincts that with no edges
//            if (edge == null) {
//                clusters.remove(candidates.get(i));
//                return true;
//            }
            System.out.printf("Edge is %s \n", edge);
            System.out.printf("candidate is %s \n", candidates.get(i));
            System.out.printf("The other is %s \n", edge.getTheOther(candidates.get(i)));
            if (candidates.contains(edge.getTheOther(candidates.get(i)))
                    && edge.getElement1().getData().getDemographic().getPopulation(RaceType.ALL)
                    + edge.getElement2().getData().getDemographic().getPopulation(RaceType.ALL)
                    < (totalPopulation / targetNumCluster)) {
                merge(edge);
                return true;
            }
        }
        return false;
    }

    private List<Cluster> filterClusters(){
        return clusters.stream()
                .filter(cluster -> cluster.getData().getDemographic().getPopulation(RaceType.ALL) <= totalPopulation / targetNumCluster)
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
        System.out.println(districts.size());
        return districts;
    }


}
