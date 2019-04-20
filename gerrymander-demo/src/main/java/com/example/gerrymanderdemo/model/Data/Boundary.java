package com.example.gerrymanderdemo.model.Data;

import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONObject;

public class Boundary implements ResponseObject {
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

    @Override
    public String toString() {
        return geoJSON.toString();
    }

    @Override
    public JSONObject toJSONObject() {
        return geoJSON;
    }
}
