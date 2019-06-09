package com.github.bewithforce.riderapp.post.requestBeans;

import java.util.List;

public class OrderWithDishes {
    private Integer id;
    private Integer status;
    private String restaurant_name;
    private Double delivery_longitude;
    private Double delivery_latitude;
    private String delivery_address;
    private String restaurant_arrival_time;
    private Double full_order_sum;
    private Double order_sum;
    private String customer_arrival_time;
    private Double restaurant_longitude;
    private Double restaurant_latitude;
    private String restaurant_address;
    private String restaurant_comment;
    private String restaurant_phone = null;
    private String customer_phone;
    private String Delivery_comment;
    private List<Dish> dishes;

    public List<Dish> getDishes() {
        return dishes;
    }

    public Integer getId() {
        return id;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public Double getDelivery_longitude() {
        return delivery_longitude;
    }

    public Double getDelivery_latitude() {
        return delivery_latitude;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public String getRestaurant_arrival_time() {
        return restaurant_arrival_time;
    }

    public Double getFull_order_sum() {
        return full_order_sum;
    }

    public Double getOrder_sum() {
        return order_sum;
    }

    public String getCustomer_arrival_time() {
        return customer_arrival_time;
    }

    public Double getRestaurant_longitude() {
        return restaurant_longitude;
    }

    public Double getRestaurant_latitude() {
        return restaurant_latitude;
    }

    public String getRestaurant_address() {
        return restaurant_address;
    }

    public String getRestaurant_comment() {
        return restaurant_comment;
    }

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public String getDelivery_comment() {
        return Delivery_comment;
    }

// Setter Methods

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setRestaurant_name(String restaurant_name) {
        this.restaurant_name = restaurant_name;
    }

    public void setDelivery_longitude(Double delivery_longitude) {
        this.delivery_longitude = delivery_longitude;
    }

    public void setDelivery_latitude(Double delivery_latitude) {
        this.delivery_latitude = delivery_latitude;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public void setRestaurant_arrival_time(String restaurant_arrival_time) {
        this.restaurant_arrival_time = restaurant_arrival_time;
    }

    public void setFull_order_sum(Double full_order_sum) {
        this.full_order_sum = full_order_sum;
    }

    public void setOrder_sum(Double order_sum) {
        this.order_sum = order_sum;
    }

    public void setCustomer_arrival_time(String customer_arrival_time) {
        this.customer_arrival_time = customer_arrival_time;
    }

    public void setRestaurant_longitude(Double restaurant_longitude) {
        this.restaurant_longitude = restaurant_longitude;
    }

    public void setRestaurant_latitude(Double restaurant_latitude) {
        this.restaurant_latitude = restaurant_latitude;
    }

    public void setRestaurant_address(String restaurant_address) {
        this.restaurant_address = restaurant_address;
    }

    public void setRestaurant_comment(String restaurant_comment) {
        this.restaurant_comment = restaurant_comment;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public void setDelivery_comment(String delivery_comment) {
        Delivery_comment = delivery_comment;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}