package com.africell.africellsurvey.helper;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class FusedLocationService extends Service {
    public Context mContext;
    private double longitude, latitude;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback locationCallback;

    public FusedLocationService(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    public void getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        //txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.i("LOCATIONRESULT", "null");
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        //txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                    }
                }
                super.onLocationResult(locationResult);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void stopLocationUpdate(){
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
