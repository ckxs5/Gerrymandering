package com.example.gerrymanderdemo.model.Response;

public class OKIdResponse extends OKResponse {
    private String id;

    public OKIdResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
