package com.github.bewithforce.riderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;
import com.github.bewithforce.riderapp.post.requestBeans.JsonWebToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GeoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CallAPI callAPI = APIClient.getClient().create(CallAPI.class);

        JsonWebToken token = new JsonWebToken();
        SharedPreferences prefs = context.getSharedPreferences("session_token", Context.MODE_PRIVATE);
        String token_string = prefs.getString("token", null);
        token.setToken(token_string);

        CourierLocation location = new CourierLocation();
        location.setLatitude(intent.getExtras().getDouble("latitude"));
        location.setLongitude(intent.getExtras().getDouble("longitude"));

        Log.e("veeeeeeee", location.getLatitude().toString() + " and " + location.getLongitude().toString());
        Call<Void> call = callAPI.locationReport(location, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("veeeeeeee", response.message());
                switch (response.code()){
                    case 401:
                        prefs.edit().clear().apply();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }
        });

    }

}