package com.github.bewithforce.riderapp.post;

import com.github.bewithforce.riderapp.post.requests.CourierLocationPOST;
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
    Observable<Void> locationReport(@Body CourierLocationPOST locationPOST);

    @POST("auth")
    Call<Void> login(@Body LoginPOST loginPOST);

    @GET("orders//get")
    Observable<OrdersPOST> getOrders();

    @GET("order//{id}")
    Observable<OrderWithDishesPOST> getDetailedOrder(@Path("id") int orderId);

    @GET("statistics")
    Observable<StatPOST> getStatiscs();

    //не описано
    @GET()
    Observable<OrdersPOST> getOrdersHistory();

    @POST("arrivedtorestaurant")
    Observable<Void> arrivedToRestaurant();

    @POST("arrivedtocustomer")
    Observable<Void> arrivedToCustomer();

}
