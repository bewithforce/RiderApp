package com.github.bewithforce.riderapp.gui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.github.bewithforce.riderapp.GeoReceiver;
import com.github.bewithforce.riderapp.GeoService;
import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.fragments.OrdersFragment;
import com.github.bewithforce.riderapp.gui.fragments.StatsFragment;

public class BaseActivity extends AppCompatActivity {

    private GeoReceiver receiver;
    private Intent geoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        BottomNavigationView view = findViewById(R.id.navigation);
        geoIntent = new Intent(this, GeoService.class);
        receiver = new GeoReceiver();
        if(!checkIfAlreadyHavePermission()){
            Log.e("veeeeeeeee", "we will receive");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
        }
        else{
            Log.e("veeeeeeeee", "already have");
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.github.bewithforce.riderapp");
            registerReceiver(receiver, filter);
            startService(geoIntent);
        }

        view.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_orders:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.base_fragment, new OrdersFragment())
                            .commit();
                    return true;
                case R.id.action_history:
                    return true;
                case R.id.action_stats:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.base_fragment, new StatsFragment())
                            .commit();
                    return true;
                case R.id.action_exit:
                    finish();
                    return true;
            }
            return false;

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private boolean checkIfAlreadyHavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 7:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
                break;
            default:
                IntentFilter filter = new IntentFilter();
                filter.addAction("com.github.bewithforce.riderapp");
                registerReceiver(receiver, filter);
                startService(geoIntent);
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
