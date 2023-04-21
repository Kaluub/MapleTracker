package com.ocdsb.mapletracker;

import com.ocdsb.mapletracker.api.FileManager;
import com.ocdsb.mapletracker.api.LocationAPI;
import com.ocdsb.mapletracker.api.WeatherAPI;

public class Config {
    public static final Boolean debugMode = false;
    public static LocationAPI locationAPI = new LocationAPI();
    public static WeatherAPI weatherAPI = new WeatherAPI();
    public static FileManager fileManager = new FileManager();
}
