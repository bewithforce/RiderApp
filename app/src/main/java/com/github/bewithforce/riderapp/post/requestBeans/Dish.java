package com.github.bewithforce.riderapp.post.requestBeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dish {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("count")
    @Expose
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}