package com.ocdsb.mapletracker.api;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationAPI {
    double latitude;
    double longitude;
    FusedLocationProviderClient locationProviderClient;

    LocationAPI() {
        latitude = 0.0;
        longitude = 0.0;

        Location location = updateLocation();
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }

    @SuppressLint("MissingPermission")
    public Location updateLocation() {
        final Location[] location = new Location[1];
        locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    location[0] = task.getResult();
                } else {
                    location[0] = null;
                }
            }
        });
        return location[0];
    }
}