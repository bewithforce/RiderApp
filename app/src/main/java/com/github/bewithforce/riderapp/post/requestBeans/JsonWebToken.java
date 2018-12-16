package com.github.bewithforce.riderapp.post.requestBeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonWebToken {

    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}