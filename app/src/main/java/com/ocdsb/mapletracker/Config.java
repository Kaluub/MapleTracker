package com.ocdsb.mapletracker;

import android.content.Context;

import com.ocdsb.mapletracker.api.FileManager;
import com.ocdsb.mapletracker.api.LocationAPI;
import com.ocdsb.mapletracker.data.StationResult;
import com.ocdsb.mapletracker.api.WeatherAPI;

import java.util.Arrays;

public class Config {
    // Tools and settings.
    public static Boolean debugMode = false;
    public static Boolean useFakeTemperature = false;
    public static Boolean useGallons = false;
    public static Boolean useFahrenheit = false;
    public static String fileName = "pins";
    public static String configFileName = "settings";
    public static String fileSeparator = ",";
    public static Boolean permissionsDisabled = false;

    // Class instances.
    public static LocationAPI locationAPI = new LocationAPI();
    public static WeatherAPI weatherAPI = new WeatherAPI();
    public static FileManager fileManager = new FileManager();
    public static StationResult stationResult = null;

    public static void loadConfig(Context context) {
        String contents = Config.fileManager.readFile(context, Config.configFileName);
        if (contents == null) {
            // The file doesn't exist yet. There are no custom settings.
            return;
        }

        for (String segment : contents.split("\n")) {
            if (segment.length() <= 0) {
                continue;
            }

            String[] parts = segment.split("=");

            if (parts.length < 2) {
                System.out.printf("Config loading found unexpected value: %s%n", segment);
                continue;
            }

            // Extract key and value while maintaining any additional equals signs.
            String key = parts[0];
            String value = String.join("=", Arrays.copyOfRange(parts, 1, parts.length));

            // Define loading behaviour here.
            // The case is the key of the setting. It should be the same as in the
            // saveConfig function. value is everything past the first equals sign.
            switch (key) {
                case "useGallons":
                    Config.useGallons = Boolean.parseBoolean(value);
                    break;
                case "useFahrenheit":
                    Config.useFahrenheit = Boolean.parseBoolean(value);
                    break;
                case "debugMode":
                    Config.debugMode = Boolean.parseBoolean(value);
                    break;
                case "fileName":
                    Config.fileName = value;
                    break;
                default:
                    System.out.printf("Config loading found unexpected value: %s%n", segment);
                    break;
            }
        }
    }

    public static void saveConfig(Context context) {
        // Define saving behaviour here. Make sure to use \n at the end of every line.
        // The file interpreter separates each line by a "=" symbol.
        String result = String.format("debugMode=%s\n", Config.debugMode) +
                String.format("useGallons=%s\n", Config.useGallons) +
                String.format("useFahrenheit=%s\n", Config.useFahrenheit) +
                String.format("fileName=%s\n", Config.fileName);

        Config.fileManager.saveFile(context, Config.configFileName, result);
    }
}
