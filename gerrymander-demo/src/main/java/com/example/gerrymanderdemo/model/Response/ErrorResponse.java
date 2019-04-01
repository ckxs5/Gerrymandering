package com.example.gerrymanderdemo.model.Response;

import com.example.gerrymanderdemo.model.Response.Response;

public class ErrorResponse extends Response {

    private String status = "error";
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
