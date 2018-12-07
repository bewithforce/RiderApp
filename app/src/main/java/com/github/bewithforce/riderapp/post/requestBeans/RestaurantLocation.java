package com.github.bewithforce.riderapp.post.requestBeans;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantLocation {

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longtitude")
    @Expose
    private Double longtitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

}
