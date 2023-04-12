package com.ocdsb.mapletracker;

import com.ocdsb.mapletracker.api.LocationAPI;
import com.ocdsb.mapletracker.api.WeatherAPI;

public class Config {
    public static LocationAPI locationAPI = new LocationAPI();
    public static WeatherAPI weatherAPI = new WeatherAPI();
}
