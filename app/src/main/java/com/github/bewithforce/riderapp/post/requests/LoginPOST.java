package com.github.bewithforce.riderapp.post.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginPOST{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pass")
    @Expose
    private String pass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}