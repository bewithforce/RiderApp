package com.github.bewithforce.riderapp.gui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.services.GeoService;
import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.fragments.OrdersFragment;
import com.github.bewithforce.riderapp.gui.fragments.StatsFragment;
import com.github.bewithforce.riderapp.tools.SessionTools;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

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
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, e -> {
            if(e.getLocationSettingsStates().isGpsUsable()) {
                startService(geoIntent);
                Log.e("veeeeBaseActivity", "location service starts");
            }else {
                Toast.makeText(this, "включите GPS", Toast.LENGTH_LONG).show();
            }
        });
        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(BaseActivity.this,
                            911);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
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
