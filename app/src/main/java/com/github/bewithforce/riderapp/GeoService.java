package com.github.bewithforce.riderapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GeoService extends Service {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    public GeoService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("veeeeeeee", "service was lost");
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        foundMeOnTheEarth();
        Log.e("veeeeeeee", "service was starting");
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
                Intent mBroadcastIntent = new Intent();
                mBroadcastIntent.setAction("com.github.bewithforce.riderapp");
                mBroadcastIntent.putExtra("latitude", location.getLatitude());
                mBroadcastIntent.putExtra("longitude", location.getLongitude());
                sendBroadcast(mBroadcastIntent);
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