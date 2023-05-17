package com.ocdsb.mapletracker;

import android.content.Context;

import com.ocdsb.mapletracker.api.FileManager;
import com.ocdsb.mapletracker.api.LocationAPI;
import com.ocdsb.mapletracker.data.StationResult;
import com.ocdsb.mapletracker.api.WeatherAPI;

public class Config {
    // Tools and settings.
    public static Boolean debugMode = true;
    public static Boolean useFakeTemperature = false;
    public static Boolean useGallons = false;
    public static String fileName = "pins";
    public static String configFileName = "settings";
    public static String fileSeparator = ",";
    // Class instances.
    public static LocationAPI locationAPI = new LocationAPI();
    public static WeatherAPI weatherAPI = new WeatherAPI();
    public static FileManager fileManager = new FileManager();
    public static StationResult stationResult = null;

    public static void loadConfig(Context context) {
        String contents = Config.fileManager.readFile(context, Config.configFileName);
        if (contents == null) {
            return;
        }

        for (String segment : contents.split("\n")) {
            if (segment.length() <= 0) {
                continue;
            }

            String[] values = segment.split("=");

            if (values.length != 2) {
                System.out.printf("Config loading found unexpected value: %s%n", segment);
                continue;
            }

            // Define loading behaviour here.
            switch (values[0]) {
                case "useGallons":
                    Config.useGallons = Boolean.parseBoolean(values[1]);
                    break;
                case "debugMode":
                    Config.debugMode = Boolean.parseBoolean(values[1]);
                    break;
                default:
                    System.out.printf("Config loading found unexpected value: %s%n", segment);
                    break;
            }
        }
    }

    public static void saveConfig(Context context) {
        String result = String.format("debugMode=%s\n", Config.debugMode) +
                String.format("useGallons=%s\n", Config.useGallons);

        Config.fileManager.saveFile(context, Config.configFileName, result);
    }
}
