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

import java.util.List;

public class LocationAPI implements LocationListener {
    public double latitude;
    public double longitude;

    public LocationAPI() {
        latitude = 0.0;
        longitude = 0.0;
    }

    public void updateLocationManager(LocationManager manager) {
        List<String> providers = manager.getAllProviders();
        Location bestLocation = null;
        for (String provider : providers) {
            try {
                Location lastKnownLocation = manager.getLastKnownLocation(provider);
                if (lastKnownLocation == null) {
                    System.out.println("Provider " + provider + " does not work.");
                    continue;
                }
                if (bestLocation == null || lastKnownLocation.getAccuracy() < bestLocation.getAccuracy()) {
                    latitude = lastKnownLocation.getLatitude();
                    longitude = lastKnownLocation.getLongitude();
                    bestLocation = lastKnownLocation;
                }
            } catch (SecurityException e) {
                System.out.println("Provider " + provider + " does not work (SecurityException).");
            }
        }
        System.out.println(latitude + ", " + longitude);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

}