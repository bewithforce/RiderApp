package com.github.bewithforce.riderapp.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SessionTools {

    public static boolean checkToken(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);

        Long millis_new = null;
        Long millis_old = null;

        try {
            Date Old = sdf.parse(preferences.getString("token_date", null));
            Date New = new Date(System.currentTimeMillis()); //or simply new Date();
            millis_old = Old.getTime();
            millis_new = New.getTime();

        } catch (Exception e) {
            Log.e("veeeeCheckToken", e.getLocalizedMessage());
        }
        if (millis_old != null && millis_new != null) {
            if (millis_new - millis_old > 32_400_000) {
                removeToken(context);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static void removeToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().remove("session_token").remove("token_date").apply();
    }

    public static void addToken(Context context, String token){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        Date date = new Date(System.currentTimeMillis());
        preferences.edit()
                .putString("session_token", token)
                .putString("token_date", sdf.format(date))
                .apply();
    }

    public static String getToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("session_token", null);
    }
}
