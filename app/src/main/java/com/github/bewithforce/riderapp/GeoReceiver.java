package com.github.bewithforce.riderapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;


public class GeoReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LatLng latLng = new LatLng(intent.getExtras().getDouble("latitude"),intent.getExtras().getDouble("longitude"));
        //отправить пост запрос locationReport
    }

}