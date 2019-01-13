package com.github.bewithforce.riderapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.github.bewithforce.riderapp.alarms.GeoAlarm;
import com.github.bewithforce.riderapp.post.requestBeans.CourierLocation;
import com.github.bewithforce.riderapp.tools.LocationTools;

public class GeoService extends Service {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private GeoAlarm alarm = new GeoAlarm();

    public GeoService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alarm.cancelAlarm(this);
        Log.e("veeeeServiceDestroy", "service was lost");
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        foundMeOnTheEarth();
        alarm.setAlarm(this);
        Log.e("veeeeServiceStart", "service was starting");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void foundMeOnTheEarth() throws SecurityException{
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                CourierLocation location1 = new CourierLocation();
                location1.setLongitude(location.getLongitude());
                location1.setLatitude(location.getLatitude());
                LocationTools.addToken(GeoService.this, location1);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}
            @Override
            public void onProviderEnabled(String s) {}
            @Override
            public void onProviderDisabled(String s) {}
        };
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,mLocationListener);
    }

}