package com.example.maplelogger.API;

import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import java.util.List;

public class LocationAPI implements LocationListener {
    double latitude;
    double longitude;

    LocationAPI() {
        latitude = 0.0;
        longitude = 0.0;
    }
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}
