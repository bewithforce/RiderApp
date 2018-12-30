package com.github.bewithforce.riderapp.gui.SplashActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.BaseActivity;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        final Intent mainIntent;

        Long millis_new = null;
        Long millis_old = null;

        try{
            Date Old = sdf.parse(preferences.getString("token_date", null));
            Date New = new Date(System.currentTimeMillis()); //or simply new Date();
            millis_old = Old.getTime();
            millis_new = New.getTime();

        } catch (Exception e) {
            Log.e("veeeee", e.getLocalizedMessage());
        }

        if(millis_old != null && millis_new != null) {
            if (millis_new - millis_old < 36_000_000) {
                mainIntent = new Intent(SplashActivity.this, BaseActivity.class);
            } else {
                mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
            }
        }
        else{
            mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        new Handler().postDelayed(() -> {
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, 1000);
    }
}
