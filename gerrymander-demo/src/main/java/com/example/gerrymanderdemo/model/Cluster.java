package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.Order;

import java.util.Collection;
import java.util.Iterator;

public class Cluster {
    private Data data;
    private Collection<Edge> edges;
    private Precinct precinct;
    private Collection<Cluster> children;

    public Cluster(Precinct precinct){
        this.precinct = precinct;
    }

    public Cluster(Cluster c1, Cluster c2){
        data = new Data(c1.data, c2.data);
        addChildren(c1,c2);
        constructEdges(c1.getEdges(),c2.getEdges(),c1,c2);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Collection<Edge> getEdges() { return edges; }

    public void setEdges(Collection<Edge> edges) {
        this.edges = edges;
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

    public void setChildren(Collection<Cluster> children) {
        this.children = children;
    }

    public Pair getBestPair(){
        // Todo
        return null;
    }

    public  void sortEdgesByJoinability(Order order){
        // Todo
    }
    //TODO: argument inconsistent with class diagram
    public void constructEdges(Collection<Edge> c1Edges, Collection<Edge> c2Edges){

    }

    public void addChildren(Cluster c1, Cluster c2){
        children.add(c1);
        children.add(c2);
    }

    public District toDistrict(){
        // Todo
        return null;
    }


}
