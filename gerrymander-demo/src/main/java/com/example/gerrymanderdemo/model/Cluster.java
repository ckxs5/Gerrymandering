package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.RaceType;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Cluster implements Comparable{
    private Data data;
    private List<Edge> edges;
    private Precinct precinct;
    private List<Cluster> children;

    public Cluster(Precinct precinct){
        this.data = precinct.getData();
        this.precinct = precinct;
        this.edges = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public Cluster(Cluster c1, Cluster c2){
        this.data = new Data(c1.getData(),c2.getData());
        this.edges = new ArrayList<>();
        this.children = new ArrayList<>();
        this.addChildren(c1);
        this.addChildren(c2);
        this.constructEdges(c1,c2);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Collection<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }

    public Collection<Cluster> getChildren() {
        return children;
    }

    public void setChildren(List<Cluster> children) {
        this.children = children;
    }

    public Edge getBestEdge(){
        try {
            Edge result = edges.get(0);
            for (Edge e : edges) {
                if (e.getJoinability() > result.getJoinability()) {
                    result = e;
                }
            }
            return result;
        } catch (IndexOutOfBoundsException ex) {
            System.out.printf("There is no edge for cluster with children %d\n", this.getChildren().size());
            return null;
        }
    }

    // add ClassDiagram
    public void constructEdges(Cluster c1, Cluster c2){
        c1.passEdges(this);
        c2.passEdges(this);
        edges = edges.stream().filter(edge -> !edge.isRedundant()).collect(Collectors.toList());
    }

    // add ClassDiagram
    private void passEdges(Cluster parentCluster){
//        System.out.println("Precinct id in cluster: "+ precinct.getId());
        for (Edge e : edges) {
            e.updateElement(this, parentCluster);
            parentCluster.addEdge(e);
        }
        this.edges.clear();
    }

    public void addChildren(Cluster c){
        children.add(c);
    }

    public Set<Precinct> getPrecincts() {
        Set<Precinct> set = new HashSet<>();
        if (precinct != null) {
            set.add(precinct);
        } else {
            for (Cluster child : children) {
                set.addAll(child.getPrecincts());
            }
        }
        return set;
    }

    public Set<String> getCounties() {
        Set<String> counties = new HashSet<>();
        Set<Precinct> precincts = getPrecincts();
        for (Precinct p : precincts) {
            counties.add(p.getCounty());
        }
        return counties;
    }

    public District toDistrict(){
        District d = new District();
        d.setData(this.data);
        d.setPrecincts(this.getPrecincts());
        d = DistrictManager.getInstance().save(d);
        for (Precinct p: d.getPrecincts()){
            p.setDistrict(d, false);
        }
        return d;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Cluster) {
            return Integer.compare(data.getDemographic().getPopulation(RaceType.ALL),
                    ((Cluster) o).getData().getDemographic().getPopulation(RaceType.ALL));
        } else {
            return -1;
        }
    }

    public Cluster getNeiWithLowestPop(){
        Cluster targert = edges.get(0).getTheOther(this);
        for (Edge e : edges){
            Cluster temp = e.getTheOther(this);
            if(temp.getData().getDemographic().getPopulation(RaceType.ALL) < targert.getData().getDemographic().getPopulation(RaceType.ALL)) {
                targert = temp;
            }
        }
        return targert;
    }

}