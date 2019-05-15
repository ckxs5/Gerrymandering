package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
public class Boundary implements ResponseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    @OrderColumn
    private double[][] geoJSON;

    public Boundary() {
    }

    public Boundary(Boundary boundary) {
        geoJSON = boundary.getGeoJSON();
    }

    public Boundary(double[][] geoJSON) {
        this.geoJSON = geoJSON;
    }

    public Boundary(Boundary b1, Boundary b2) {
        //TODO
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double[][] getGeoJSON() {
        return geoJSON;
    }

    public void setGeoJSON(double[][] geoJSON) {
        this.geoJSON = geoJSON;
    }

    public void add(Boundary other) {
        //TODO
    }

    public Boundary remove(Boundary other) {
        //TODO
        return other;
    }

    @Override
    public String toString() {
        return geoJSON.toString();
    }

    @Override
    public JSONObject toJSONObject() {
        //try {
            return null;
//        }catch (JSONException ex) {
//            System.out.printf("Exception catched when returning a geoJson: %s \n", ex.getMessage());
//            return null;
//        }
    }
}
