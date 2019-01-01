package com.github.bewithforce.riderapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CallAPI callAPI = APIClient.getClient().create(CallAPI.class);

        SharedPreferences prefs = context.getSharedPreferences("session_token", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        CourierLocation location = new CourierLocation();
        location.setLatitude(intent.getExtras().getDouble("latitude"));
        location.setLongitude(intent.getExtras().getDouble("longitude"));

        Log.e("veeeeeeee", location.getLatitude().toString() + " and " + location.getLongitude().toString());
        Call<Void> call = callAPI.locationReport(token, location);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("veeeeeeeeFail", response.message());
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