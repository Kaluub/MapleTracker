package com.ocdsb.mapletracker.data;

import java.util.ArrayList;
import java.util.Date;

public class StationResult {
    // Stores the used results from fetching with the WeatherAPI.
    public Date createdAt = new Date();
    public double temperature;
    public double high;
    public double low;
    public String stationID;
    public String provinceCode;
    public ArrayList<Integer> forecastHighs;
    public ArrayList<Integer> forecastLows;
}

