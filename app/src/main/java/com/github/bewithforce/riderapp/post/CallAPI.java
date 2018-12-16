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
    Call<Void> locationReport(@Body CourierLocation location, @Header("token") JsonWebToken token);

    @POST("auth")
    Call<JsonWebToken> login(@Body Login login);

    @GET("orders//get")
    Observable<Orders> getOrders(@Header("token") JsonWebToken token);

    @GET("order//{id}")
    Observable<OrderWithDishes> getDetailedOrder(@Path("id") int orderId, @Header("token")JsonWebToken token);

    @GET("statistics")
    Call<Stat> getStatiscs(@Header("token") JsonWebToken token);

    //не описано
    @GET()
    Observable<Orders> getOrdersHistory(@Header("token")JsonWebToken token);

    @POST("arrivedtorestaurant")
    Observable<Void> arrivedToRestaurant(@Header("token")JsonWebToken token);

    @POST("arrivedtocustomer")
    Observable<Void> arrivedToCustomer(@Header("token")JsonWebToken token);

}
