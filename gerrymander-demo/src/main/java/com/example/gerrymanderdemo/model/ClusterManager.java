package com.example.gerrymanderdemo.model;
import com.example.gerrymanderdemo.model.Enum.GraphPartition;
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
    private GraphPartition state = GraphPartition.GRAPH_PARTITION;
    int runCount = 0;

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

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public int getTargetNumCluster() {
        return targetNumCluster;
    }

    public void setTargetNumCluster(int targetNumCluster) {
        this.targetNumCluster = targetNumCluster;
    }

    public RaceType getCommunityOfInterest() {
        return communityOfInterest;
    }

    public void setCommunityOfInterest(RaceType communityOfInterest) {
        this.communityOfInterest = communityOfInterest;
    }

    public int getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(int totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public int getTargetPopulation() {
        return targetPopulation;
    }

    public void setTargetPopulation(int targetPopulation) {
        this.targetPopulation = targetPopulation;
    }

    public GraphPartition getState() {
        return state;
    }

    public void setState(GraphPartition state) {
        this.state = state;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
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
        switch (state) {
            case GRAPH_PARTITION:
                while (!done) {
                    System.out.printf("Partition Run : %d \n", ++count);
                    done = !graphPartition();
                }
            case FORCE_TO_TARGET_NUMBER:
                while(clusters.size() > targetNumCluster){
                    forceToTargetNum();
                }
//            case BALANCE_POPULATION:
//                count = 0;
//                toDistricts();
//                while (balancePopulation() && count < targetNumCluster){
//                    System.out.printf("Balancing:  %d \n", ++count);
//                }
        }

    }

    public boolean runOnce() {
        switch (state) {
            case GRAPH_PARTITION:
                System.out.printf("Partition RunONCE: %d \n", ++runCount);
                if(!graphPartition()){
                    state = GraphPartition.FORCE_TO_TARGET_NUMBER;
                }
                return true;
            case FORCE_TO_TARGET_NUMBER:
                if(clusters.size() > targetNumCluster){
                    forceToTargetNum();
                } else {
                    toDistricts();
                    runCount = 0;
                    state = GraphPartition.BALANCE_POPULATION;
                }
                return true;
            case BALANCE_POPULATION:
                if (balancePopulation() && runCount < targetNumCluster){
                    System.out.printf("Balancing:  %d \n", ++runCount);
                    return true;
                }
        }
        return false;
    }

    private void forceToTargetNum() {
        Cluster lowestPop = getClusterWithLowestPop();
        Cluster lowNeiPop = lowestPop.getNeiWithLowestPop();
        Pair<Cluster> pair = new Pair<>(lowestPop, lowNeiPop);
        merge(pair);
    }

    public boolean graphPartition() {
        System.out.println("CLusterSize: " + clusters.size());
        System.out.println("targetNumCLuter: " + targetNumCluster);
        if (clusters.size() <= targetNumCluster) {
            System.out.println("clusters.size(): " + clusters.size());
            return false;
        }
        boolean combined = false;
        List<Cluster> candidates = filterClusters();
        int count = 0;
        while (candidates.size() > 1 && count < candidates.size() - 1) {
//            Cluster target = candidates.get((int)(Math.random() * candidates.size()));
            Cluster target = candidates.get(count);
//            System.out.println("Target candidate: " + target);
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

    public boolean balancePopulation() {
        District from = getDistrictWithHighestPop();
        if (from.getData().getDemographic().getPopulation(RaceType.ALL) <= targetPopulation) {
            return false;
        }

        District to = from.getLowestPopNeigbour();
        while (from.getData().getDemographic().getPopulation(RaceType.ALL) > targetPopulation
            || to.getData().getDemographic().getPopulation(RaceType.ALL) < targetPopulation) {
            if(!from.passPrecinct(to)) {
                return true;
            }
        }
        return true;
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

    private Cluster getClusterWithLowestPop() {
        Cluster target = clusters.get(0);
        for(Cluster cl : clusters) {
            if(cl.getData().getDemographic().getPopulation(RaceType.ALL) < target.getData().getDemographic().getPopulation(RaceType.ALL)) {
                target = cl;
            }
        }
        return target;
    }

    public List<District> toDistricts() {
        List<District> districts = new ArrayList<>();
        for (Cluster c : clusters) {
            districts.add(c.toDistrict());
        }

        this.districts = districts;
        return districts;
    }


}
