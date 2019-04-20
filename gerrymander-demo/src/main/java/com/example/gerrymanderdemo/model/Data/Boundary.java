package com.example.gerrymanderdemo.model.Data;

import org.json.JSONObject;

public class Boundary {
    JSONObject geoJSON;

    public Boundary(JSONObject geoJSON) {
        this.geoJSON = geoJSON;
    }

    public JSONObject getGeoJSON() {
        return geoJSON;
    }

    public void setGeoJSON(JSONObject geoJSON) {
        this.geoJSON = geoJSON;
    }
}
