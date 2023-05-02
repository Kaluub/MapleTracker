package com.ocdsb.mapletracker.api;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

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
            // We can determine the most accurate location provider to provide the best service.
            try {
                Location lastKnownLocation = manager.getLastKnownLocation(provider);
                if (lastKnownLocation == null) {
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
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Keep the location up-to-date.
        System.out.println("Location updated.");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

}