package com.ocdsb.mapletracker;

import com.ocdsb.mapletracker.api.FileManager;
import com.ocdsb.mapletracker.api.LocationAPI;
import com.ocdsb.mapletracker.data.StationResult;
import com.ocdsb.mapletracker.api.WeatherAPI;

public class Config {
    // Tools and settings.
    public static final Boolean debugMode = false;
    public static Boolean useFakeTemperature = false;
    public static Boolean useGallons = true;
    public static String fileName = "pins";
    public static String fileSeparator = ",";
    // Class instances.
    public static LocationAPI locationAPI = new LocationAPI();
    public static WeatherAPI weatherAPI = new WeatherAPI();
    public static FileManager fileManager = new FileManager();
    public static StationResult stationResult = null;
}
