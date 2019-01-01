package com.github.bewithforce.riderapp.post;

import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;
import com.github.bewithforce.riderapp.post.requestBeans.JsonWebToken;
import com.github.bewithforce.riderapp.post.requestBeans.Login;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.post.requestBeans.OrderWithDishes;
import com.github.bewithforce.riderapp.post.requestBeans.Orders;
import com.github.bewithforce.riderapp.post.requestBeans.Stat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CallAPI {

    @POST("update_location")
    Call<Void> locationReport(@Header("authorization")String token, @Body CourierLocation location);

    @POST("auth")
    Call<JsonWebToken> login(@Body Login login);

    @GET("orders/get")
    Call<List<Order>> getOrders(@Header("authorization") String token);

    @GET("order/{id}")
    Call<OrderWithDishes> getDetailedOrder(@Header("authorization")String token, @Path("id") int orderId);

    @GET("statistics")
    Call<Stat> getStatiscs(@Header("token") String token);

    //не описано
    @GET()
    Call<Orders> getOrdersHistory(@Header("authorization")String token);

    @POST("arrivedtorestaurant")
    Call<Void> arrivedToRestaurant(@Header("authorization")String token);

    @POST("arrivedtocustomer")
    Call<Void> arrivedToCustomer(@Header("authorization")String token);

}
