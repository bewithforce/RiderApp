package com.github.bewithforce.riderapp.post.requests;

import java.util.List;

import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersPOST {

    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
