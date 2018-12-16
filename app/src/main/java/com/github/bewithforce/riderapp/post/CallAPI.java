package com.github.bewithforce.riderapp.post;

import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;
import com.github.bewithforce.riderapp.post.requestBeans.JsonWebToken;
import com.github.bewithforce.riderapp.post.requestBeans.Login;
import com.github.bewithforce.riderapp.post.requestBeans.OrderWithDishes;
import com.github.bewithforce.riderapp.post.requestBeans.Orders;
import com.github.bewithforce.riderapp.post.requestBeans.Stat;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CallAPI {

    @POST("update_location")
    Call<Void> locationReport(@Header("token")String token, @Body CourierLocation location);

    @POST("auth")
    Call<JsonWebToken> login(@Body Login login);

    @GET("orders//get")
    Observable<Orders> getOrders(@Header("token") String token);

    @GET("order//{id}")
    Observable<OrderWithDishes> getDetailedOrder(@Path("id") int orderId, @Header("token")String token);

    @GET("statistics")
    Call<Stat> getStatiscs(@Header("token") String token);

    //не описано
    @GET()
    Observable<Orders> getOrdersHistory(@Header("token")String token);

    @POST("arrivedtorestaurant")
    Observable<Void> arrivedToRestaurant(@Header("token")String token);

    @POST("arrivedtocustomer")
    Observable<Void> arrivedToCustomer(@Header("token")String token);

}
