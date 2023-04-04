package com.example.maplelogger.API;

import android.location.Location;
import android.location.LocationListener;

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
