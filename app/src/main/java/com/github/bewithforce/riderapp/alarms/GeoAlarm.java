package com.github.bewithforce.riderapp.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;
import com.github.bewithforce.riderapp.tools.LocationTools;
import com.github.bewithforce.riderapp.tools.SessionTools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeoAlarm extends BroadcastReceiver {
    String token;
    @Override
    public void onReceive(Context context, Intent intent) {
        CallAPI callAPI = APIClient.getClient().create(CallAPI.class);

        Log.e("veeeeAlarmWork", "smth");
        if(token == null) {
            token = SessionTools.getToken(context);
        }
        CourierLocation location = LocationTools.getToken(context);

        Log.e("veeeeGeoAlarm", location.getLatitude().toString() + " and " + location.getLongitude().toString());
        Call<Void> call = callAPI.locationReport(token, location);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.e("veeeeLocationSend", response.message());
                switch (response.code()){
                    case 401:
                        SessionTools.removeToken(context);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, GeoAlarm.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 1, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, GeoAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
