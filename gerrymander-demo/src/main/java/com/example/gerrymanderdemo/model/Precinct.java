package com.example.gerrymanderdemo.model;

import com.example.gerrymanderdemo.model.Data.Data;
import com.example.gerrymanderdemo.model.Response.ResponseObject;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Set;


public class Precinct implements ResponseObject {


    String id;

    String name;

    Data data;

    Set<Precinct> neigbours;

    public Precinct() {
    }

    public Precinct(String id, String name, Data data, Set<Precinct> neigbours) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.neigbours = neigbours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Set<Precinct> getNeigbours() {
        return neigbours;
    }

    public void setNeigbours(Set<Precinct> neigbours) {
        this.neigbours = neigbours;
    }

    public String getBoundary() {
        return data.getBoundary().toJSONObject().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJSONObject() {
        try{
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("data", data.toJSONObject());
            //TODO: put neighbours into json
            return json;
        } catch (JSONException ex) {
            System.out.println("Unexpected error occurs when converting precinct to JSON object");
            return null;
        }
    }

}
