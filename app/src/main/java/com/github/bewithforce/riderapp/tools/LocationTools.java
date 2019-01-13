package com.github.bewithforce.riderapp.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;

public class LocationTools {
    

    public static void removeToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove("courier_latitude").remove("courier_longitude").apply();
    }

    public static void addToken(Context context, CourierLocation location){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putString("courier_latitude", Double.toString(location.getLatitude()))
                .putString("courier_longitude", Double.toString(location.getLongitude()))
                .apply();
    }

    public static CourierLocation getToken(Context context){
        CourierLocation location = new CourierLocation();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        location.setLatitude(Double.parseDouble(preferences.getString("courier_latitude", null)));
        location.setLongitude(Double.parseDouble(preferences.getString("courier_longitude", null)));
        return location;
    }
}