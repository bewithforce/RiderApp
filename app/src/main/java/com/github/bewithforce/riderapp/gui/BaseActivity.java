package com.github.bewithforce.riderapp.gui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.services.GeoService;
import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.fragments.OrdersFragment;
import com.github.bewithforce.riderapp.gui.fragments.StatsFragment;
import com.github.bewithforce.riderapp.tools.SessionTools;

public class BaseActivity extends AppCompatActivity {

    private Intent geoIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        BottomNavigationView view = findViewById(R.id.navigation);
        geoIntent = new Intent(getApplicationContext(), GeoService.class);
        if(!checkIfAlreadyHavePermission()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
        }
        else{
            startService(geoIntent);
        }
        view.setOnNavigationItemSelectedListener(item -> {
            Fragment fragmentInFrame = getSupportFragmentManager()
                    .findFragmentById(R.id.base_fragment);
            switch (item.getItemId()) {
                case R.id.action_orders:
                    if(!(fragmentInFrame instanceof OrdersFragment)) {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.base_fragment, new OrdersFragment())
                                .commit();
                    }
                    return true;
                case R.id.action_history:
                    return true;
                case R.id.action_stats:
                    if(!(fragmentInFrame instanceof StatsFragment)) {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.base_fragment, new StatsFragment())
                                .commit();
                    }
                    return true;
                case R.id.action_exit:
                    SessionTools.removeToken(this);
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;

        });
        view.setSelectedItemId(R.id.action_orders);

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
                startService(geoIntent);
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("veeeeBaseActivityDied","all is bad");
        stopService(geoIntent);
    }
}
