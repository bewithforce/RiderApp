package com.github.bewithforce.riderapp.gui.SplashActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.BaseActivity;
import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;

import static com.github.bewithforce.riderapp.tools.SessionTools.checkToken;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Intent mainIntent;
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                && conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            finish();
            Toast.makeText(this, "Необходим интернет", Toast.LENGTH_LONG).show();
            return;
        }
        if (checkToken(getBaseContext())) {
            mainIntent = new Intent(SplashActivity.this, BaseActivity.class);
        } else {
            mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        new Handler().postDelayed(() -> {
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, 1000);
    }
}
