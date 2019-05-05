package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
public class Boundary implements ResponseObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 100)
    String id;
    String geoJSON;

    public Boundary() {
    }

    public Boundary(Boundary boundary) {
        geoJSON = boundary.getGeoJSON();
    }

    public Boundary(String geoJSON) {
        this.geoJSON = geoJSON;
    }

    public Boundary(Boundary b1, Boundary b2) {
        geoJSON = b1.geoJSON + b2.geoJSON;
    }

    public String getGeoJSON() {
        return geoJSON;
    }

    public void setGeoJSON(String geoJSON) {
        this.geoJSON = geoJSON;
    }

    public void add(Boundary other) {
        //TODO: Implement graph
        geoJSON += other.getGeoJSON();
    }

    public Boundary remove(Boundary other) {
        geoJSON = geoJSON.replace(other.getGeoJSON(), "");
        return other;
    }

    @Override
    public String toString() {
        return geoJSON.toString();
    }

    @Override
    public JSONObject toJSONObject() {
        try {
            return new JSONObject(geoJSON);
        }catch (JSONException ex) {
            System.out.printf("Exception catched when returning a geoJson: %s \n", ex.getMessage());
            return null;
        }
    }
}
