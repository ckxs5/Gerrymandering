package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Boundary implements ResponseObject {
    @Id
    String id;
    String geoJSON;

    public Boundary(String geoJSON) {
        this.geoJSON = geoJSON;
    }

    public String getGeoJSON() {
        return geoJSON;
    }

    public void setGeoJSON(String geoJSON) {
        this.geoJSON = geoJSON;
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
