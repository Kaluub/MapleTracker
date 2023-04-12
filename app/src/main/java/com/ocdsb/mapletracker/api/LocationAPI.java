package com.ocdsb.mapletracker.api;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class LocationAPI implements LocationListener {
    private LocationManager locationManager;
    private String provider;
    public double latitude;
    public double longitude;

    public LocationAPI() {
        latitude = 0.0;
        longitude = 0.0;
    }

    public void updateLocationManager(LocationManager manager) {
        locationManager = manager;
        provider = locationManager.getBestProvider(new Criteria(), false);
        try {
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation == null) {
                return;
            }
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
            System.out.println(latitude + ", " + longitude);
        } catch (SecurityException e) {
            System.out.println("Could not get location.");
            latitude = 0.0;
            longitude = 0.0;
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}