package com.github.bewithforce.riderapp.post.requestBeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stat {

    @SerializedName("orders_delivered_per_shift")
    @Expose
    private Integer deliveryOrders;
    @SerializedName("summ")
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