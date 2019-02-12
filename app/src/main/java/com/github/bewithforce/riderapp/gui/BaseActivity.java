package com.github.bewithforce.riderapp.gui;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.bewithforce.riderapp.gui.LogInActivity.LoginActivity;
import com.github.bewithforce.riderapp.post.APIClient;
import com.github.bewithforce.riderapp.post.CallAPI;
import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;
import com.github.bewithforce.riderapp.R;
import com.github.bewithforce.riderapp.gui.fragments.OrdersFragment;
import com.github.bewithforce.riderapp.gui.fragments.StatsFragment;
import com.github.bewithforce.riderapp.post.requestBeans.Order;
import com.github.bewithforce.riderapp.tools.FileTools;
import com.github.bewithforce.riderapp.tools.LocationTools;
import com.github.bewithforce.riderapp.tools.SessionTools;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {

    private Timer locationTimer;
    private TimerTask locationTask;
    private Timer ordersTimer;
    private TimerTask ordersTask;
    private List<Order> oldOrders;
    private static boolean activityVisible;

    private CallAPI callAPI;
    private String token;
    private Handler mTimerHandler = new Handler();
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        BottomNavigationView view = findViewById(R.id.navigation);
        if (!checkIfAlreadyHavePermission()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
        }
        callAPI = APIClient.getClient().create(CallAPI.class);
        token = SessionTools.getToken(BaseActivity.this);
        view.setOnNavigationItemSelectedListener(item -> {
            Fragment fragmentInFrame = getSupportFragmentManager()
                    .findFragmentById(R.id.base_fragment);
            switch (item.getItemId()) {
                case R.id.action_orders:
                    startOrdersTimer();
                    if (!(fragmentInFrame instanceof OrdersFragment)) {
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.base_fragment, new OrdersFragment())
                                .commit();
                    }
                    return true;
                case R.id.action_stats:
                    stopTimer(ordersTimer);
                    if (!(fragmentInFrame instanceof StatsFragment)) {
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
        activityVisible = true;
        if (checkIfAlreadyHavePermission()) {
            start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer(locationTimer);
        stopTimer(ordersTimer);
        mLocationManager.removeUpdates(mLocationListener);
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
                } else {
                    start();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 911) {
            if (resultCode == Activity.RESULT_OK) {
                if(mLocationManager == null) {
                    foundMeOnTheEarth();
                    startLocationTimer();
                }
            } else {
                Toast.makeText(this, "включите GPS", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void start() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnCompleteListener(e -> {
            try {
                task.getResult(ApiException.class);
                if (mLocationManager == null) {
                    foundMeOnTheEarth();
                    startLocationTimer();
                }
            } catch (ApiException ex) {
                switch (ex.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException =
                                    (ResolvableApiException) ex;
                            resolvableApiException
                                    .startResolutionForResult(BaseActivity.this,
                                            911);
                        } catch (IntentSender.SendIntentException error) {
                            finish();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        finish();
                        break;
                    default:
                        if (mLocationManager == null) {
                            foundMeOnTheEarth();
                            startLocationTimer();
                        }
                }
            }
        });
    }

    private void startLocationTimer() {
        if(locationTimer == null) {
            locationTimer = new Timer();
            locationTask = new TimerTask() {
                @Override
                public void run() {
                    mTimerHandler.post(() -> {
                        CourierLocation location = LocationTools.getToken(BaseActivity.this);
                        if(location != null) {
                            Call<Void> call = callAPI.locationReport(token, location);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    switch (response.code()) {
                                        case 401:
                                            SessionTools.removeToken(getBaseContext());
                                            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    call.cancel();
                                }
                            });
                        }
                        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                                && conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
                            finish();
                            Toast.makeText(BaseActivity.this, "Необходим интернет", Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                }
            };
            locationTimer.schedule(locationTask, 1, 30000);
        }
    }

    private void startOrdersTimer() {
        stopTimer(ordersTimer);
        ordersTimer = new Timer();
        ordersTask = new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.post(() -> {
                    Call<List<Order>> call = callAPI.getOrders(token);
                    call.enqueue(new Callback<List<Order>>() {
                        @Override
                        public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                            switch (response.code()) {
                                case 200:
                                    try {
                                        List<Order> orders = response.body();
                                        if (oldOrders == null) {
                                            oldOrders = FileTools.readFromFile(BaseActivity.this);
                                        }
                                        if (!orders.equals(oldOrders)) {
                                            FileTools.writeToFile(response, BaseActivity.this);
                                            oldOrders = orders;
                                            if (!activityVisible) {
                                                showNotification("новый заказ", "сменилась точка назначения");
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.e("veeeeCallOrdersBodyFail", e.getMessage());
                                    }
                                    break;
                                case 401:
                                    SessionTools.removeToken(getBaseContext());
                                    Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Order>> call, Throwable t) {
                            Log.e("veeeeCallOrdersFail", t.getLocalizedMessage());

                            call.cancel();
                        }
                    });
                });
            }
        };
        ordersTimer.schedule(ordersTask, 1, 10000);
    }

    void showNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), BaseActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void stopTimer(Timer timer) {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }

    private void foundMeOnTheEarth() throws SecurityException{
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                CourierLocation location1 = new CourierLocation();
                location1.setLongitude(location.getLongitude());
                location1.setLatitude(location.getLatitude());
                LocationTools.addToken(BaseActivity.this, location1);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {
                start();
            }
        };
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,30000,50,mLocationListener);
    }
}
