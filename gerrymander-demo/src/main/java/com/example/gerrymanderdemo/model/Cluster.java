package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Enum.Order;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Cluster{
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
        this.constructEdges(c1,c2,this);
        this.addChildren(c1,c2);
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

    public Pair getBestPair(){
        sortEdgesByJoinability();
        return this.edges.get(0).getPair();
    }

    // Class Diagram:
    public  void sortEdgesByJoinability(){
        Collections.sort(this.edges);
    }

    // add ClassDiagram
    public void constructEdges(Cluster c1, Cluster c2, Cluster newCluster){
        updateClusterEdge(c1,c2,newCluster);
        updateClusterEdge(c2,c1,newCluster);
    }

    // add ClassDiagram
    public void updateClusterEdge(Cluster oldC1, Cluster oldC2, Cluster newCluster){
        for(Edge edge: oldC1.getEdges()){
            Cluster c1 = edge.getElement1();
            Cluster c2 = edge.getElement2();
            if(c1 == oldC1){
                edge.updateEdge(newCluster,c2);
            }
            else{
                edge.updateEdge(c1,newCluster);
            }
            if(!((c1 == oldC1 && c2 == oldC2) || (c1 == oldC2 && c2 == oldC1)))
                newCluster.getEdges().add(edge);
        }
    }

    public void addChildren(Cluster c1, Cluster c2){
        children.add(c1);
        children.add(c2);
    }

    public District toDistrict(){
        District d = new District();

        ArrayList<Cluster> openList = new ArrayList<>();
        openList.addAll(this.children);
        while(!openList.isEmpty()){
            ArrayList<Cluster> tempList = new ArrayList<>();
            for(Cluster c: openList){
                if(c.getPrecinct() != null){
                    d.addPrecinct(c.getPrecinct());
                }
                tempList.addAll(c.getChildren());
            }
            openList = tempList;
        }
        return d;
    }

}
