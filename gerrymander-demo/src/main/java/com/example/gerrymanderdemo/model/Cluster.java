package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.RaceType;
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
        this.constructEdges(c1,c2);
        this.addChildren(c1);
        this.addChildren(c2);
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
        sortEdgesByJoinability();
        return this.edges.get(0);
    }

    // Class Diagram:
    public  void sortEdgesByJoinability(){
        Collections.sort(this.edges);
    }

    // add ClassDiagram
    public void constructEdges(Cluster c1, Cluster c2){
        c1.passEdges(this);
        c2.passEdges(this);
        edges = edges.stream().filter(edge -> !edge.isRedundant()).collect(Collectors.toList());
    }

    // add ClassDiagram
    private void passEdges(Cluster parentCluster){
        for (Edge e : edges) {
            e.updateElment(this, parentCluster);
        }
        List<Edge> temp = edges;
        this.edges = null;
        parentCluster.setEdges(temp);
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
}