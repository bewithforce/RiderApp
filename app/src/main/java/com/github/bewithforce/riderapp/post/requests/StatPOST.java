package com.github.bewithforce.riderapp.post.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatPOST {

    @SerializedName("delivery_orders")
    @Expose
    private Integer deliveryOrders;
    @SerializedName("received_money")
    @Expose
    private Double receivedMoney;

    public Integer getDeliveryOrders() {
        return deliveryOrders;
    }

    public void setDeliveryOrders(Integer deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
    }

    public Double getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(Double receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

}