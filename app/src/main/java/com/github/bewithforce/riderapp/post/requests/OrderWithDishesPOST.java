package com.github.bewithforce.riderapp.post.requests;

import java.util.List;

import com.github.bewithforce.riderapp.post.requestBeans.CustomerLocation;
import com.github.bewithforce.riderapp.post.requestBeans.Dish;
import com.github.bewithforce.riderapp.post.requestBeans.RestaurantLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderWithDishesPOST {

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
    @SerializedName("restaurant_comment")
    @Expose
    private String restaurantComment;
    @SerializedName("restaurant_phone")
    @Expose
    private String restaurantPhone;
    @SerializedName("full_ordrer_sum")
    @Expose
    private String fullOrdrerSum;
    @SerializedName("customer_location")
    @Expose
    private CustomerLocation customerLocation;
    @SerializedName("customer_adress")
    @Expose
    private String customerAdress;
    @SerializedName("customer_phone")
    @Expose
    private String customerPhone;
    @SerializedName("ordrer_sum")
    @Expose
    private String ordrerSum;
    @SerializedName("customer_arival_time")
    @Expose
    private String customerArivalTime;
    @SerializedName("dishes")
    @Expose
    private List<Dish> dishes = null;

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

    public String getRestaurantComment() {
        return restaurantComment;
    }

    public void setRestaurantComment(String restaurantComment) {
        this.restaurantComment = restaurantComment;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getFullOrdrerSum() {
        return fullOrdrerSum;
    }

    public void setFullOrdrerSum(String fullOrdrerSum) {
        this.fullOrdrerSum = fullOrdrerSum;
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

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getOrdrerSum() {
        return ordrerSum;
    }

    public void setOrdrerSum(String ordrerSum) {
        this.ordrerSum = ordrerSum;
    }

    public String getCustomerArivalTime() {
        return customerArivalTime;
    }

    public void setCustomerArivalTime(String customerArivalTime) {
        this.customerArivalTime = customerArivalTime;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

}
