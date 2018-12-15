package com.github.bewithforce.riderapp.post;

import com.github.bewithforce.riderapp.post.requests.CourierLocationPOST;
import com.github.bewithforce.riderapp.post.requests.JsonWebToken;
import com.github.bewithforce.riderapp.post.requests.LoginPOST;
import com.github.bewithforce.riderapp.post.requests.OrderWithDishesPOST;
import com.github.bewithforce.riderapp.post.requests.OrdersPOST;
import com.github.bewithforce.riderapp.post.requests.StatPOST;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CallAPI {

    @POST("update_location")
    Observable<Void> locationReport(@Body CourierLocationPOST locationPOST, @Body JsonWebToken token);

    @POST("auth")
    Call<JsonWebToken> login(@Body LoginPOST loginPOST);

    @GET("orders//get")
    Observable<OrdersPOST> getOrders(@Body JsonWebToken token);

    @GET("order//{id}")
    Observable<OrderWithDishesPOST> getDetailedOrder(@Path("id") int orderId, @Body JsonWebToken token);

    @GET("statistics")
    Observable<StatPOST> getStatiscs(@Body JsonWebToken token);

    //не описано
    @GET()
    Observable<OrdersPOST> getOrdersHistory(@Body JsonWebToken token);

    @POST("arrivedtorestaurant")
    Observable<Void> arrivedToRestaurant(@Body JsonWebToken token);

    @POST("arrivedtocustomer")
    Observable<Void> arrivedToCustomer(@Body JsonWebToken token);

}
