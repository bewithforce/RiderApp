package com.github.bewithforce.riderapp.post.requestBeans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("restaurant_location")
    @Expose
    private RestaurantLocation restaurantLocation;
    @SerializedName("restaurant_adress")
    @Expose
    private String restaurantAdress;
    @SerializedName("restaurant_arival_time")
    @Expose
    private String restaurantArivalTime;
    @SerializedName("customer_location")
    @Expose
    private CustomerLocation customerLocation;
    @SerializedName("customer_adress")
    @Expose
    private String customerAdress;
    @SerializedName("customer_arival_time")
    @Expose
    private String customerArivalTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public RestaurantLocation getRestaurantLocation() {
        return restaurantLocation;
    }

    public void setRestaurantLocation(RestaurantLocation restaurantLocation) {
        this.restaurantLocation = restaurantLocation;
    }

    public String getRestaurantAdress() {
        return restaurantAdress;
    }

    public void setRestaurantAdress(String restaurantAdress) {
        this.restaurantAdress = restaurantAdress;
    }

    public String getRestaurantArivalTime() {
        return restaurantArivalTime;
    }

    public void setRestaurantArivalTime(String restaurantArivalTime) {
        this.restaurantArivalTime = restaurantArivalTime;
    }

    public CustomerLocation getCustomerLocation() {
        return customerLocation;
    }

    public void setCustomerLocation(CustomerLocation customerLocation) {
        this.customerLocation = customerLocation;
    }

    public String getCustomerAdress() {
        return customerAdress;
    }

    public void setCustomerAdress(String customerAdress) {
        this.customerAdress = customerAdress;
    }

    public String getCustomerArivalTime() {
        return customerArivalTime;
    }

    public void setCustomerArivalTime(String customerArivalTime) {
        this.customerArivalTime = customerArivalTime;
    }

}