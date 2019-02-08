package com.github.bewithforce.riderapp.post.requestBeans;

import java.io.Serializable;

public class Order implements Serializable {
    private int id;
    private int status;
    private double delivery_longitude;
    private double delivery_latitude;
    private String delivery_address;
    private String restaurant_arrival_time;
    private String customer_arrival_time;
    private double restaurant_longitude;
    private double restaurant_latitude;
    private String restaurant_address;

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public double getDelivery_longitude() {
        return delivery_longitude;
    }

    public double getDelivery_latitude() {
        return delivery_latitude;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public String getRestaurant_arrival_time() {
        return restaurant_arrival_time;
    }

    public String getCustomer_arrival_time() {
        return customer_arrival_time;
    }

    public double getRestaurant_longitude() {
        return restaurant_longitude;
    }

    public double getRestaurant_latitude() {
        return restaurant_latitude;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    // Setter Methods

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDelivery_longitude(double delivery_longitude) {
        this.delivery_longitude = delivery_longitude;
    }

    public void setDelivery_latitude(double delivery_latitude) {
        this.delivery_latitude = delivery_latitude;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public void setRestaurant_arrival_time(String restaurant_arrival_time) {
        this.restaurant_arrival_time = restaurant_arrival_time;
    }

    public void setCustomer_arrival_time(String customer_arrival_time) {
        this.customer_arrival_time = customer_arrival_time;
    }

    public void setRestaurant_longitude(double restaurant_longitude) {
        this.restaurant_longitude = restaurant_longitude;
    }

    public void setRestaurant_latitude(double restaurant_latitude) {
        this.restaurant_latitude = restaurant_latitude;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }
}