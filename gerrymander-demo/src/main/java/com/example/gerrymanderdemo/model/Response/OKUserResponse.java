package com.example.gerrymanderdemo.model.Response;

import com.example.gerrymanderdemo.model.User.User;

public class OKUserResponse extends OKResponse {
    private User user;

    public OKUserResponse(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
