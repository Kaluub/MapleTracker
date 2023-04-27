package com.ocdsb.mapletracker.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.data.StationResult;

import org.w3c.dom.Document;

import java.util.Date;

public class WeatherAPI {
    Parser parser;

    public WeatherAPI() {
        parser = new Parser();
    }

    public double getDistance(JsonObject properties) {
        double elementLatitude = properties.get("Latitude").getAsDouble();
        double elementLongitude = properties.get("Longitude").getAsDouble();
        return Math.sqrt(Math.pow(Config.locationAPI.latitude - elementLatitude, 2) + Math.pow(Config.locationAPI.longitude - elementLongitude, 2));
    }

    public String[] getClosestStationDetails() {
        JsonObject webData = parser.getJSONFromURL("https://collaboration.cmc.ec.gc.ca/cmc/cmos/public_doc/msc-data/citypage-weather/site_list_en.geojson");
        JsonArray featuresArray = webData.getAsJsonArray("features");
        // Defaults to Ottawa in case you're not on Earth.
        String bestFeatureId = "s0000430";
        String bestProvinceCode = "ON";
        // The furthest possible distance should only be 180, so this should always be a bigger distance.
        double closestDistance = 100000;

        for (JsonElement element : featuresArray) {
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            double distance = getDistance(properties);
            if (distance < closestDistance) {
                System.out.println("New closer distance! " + bestFeatureId + " (" + bestProvinceCode + ") is " + closestDistance);
                bestFeatureId = properties.get("Codes").getAsString();
                bestProvinceCode = properties.get("Province Codes").getAsString();
                closestDistance = distance;
            }
        }

        String[] result = new String[2];
        result[0] = bestFeatureId;
        result[1] = bestProvinceCode;
        return result;
    }

    public StationResult getStation(String stationID, String provinceCode) {
        // Check if the StationResult cached instance is less than 1 minute old.
        if (Config.stationResult != null &&
                new Date().getTime() - Config.stationResult.createdAt.getTime() < 60000)
            return Config.stationResult;

        // Uses the station ID to get the weather from that station.
        Document doc = parser.getXMLFromURL(String.format("https://dd.weather.gc.ca/citypage_weather/xml/%s/%s_e.xml", provinceCode, stationID));
        if (doc == null) {
            // If there is no available Document, return null.
            return null;
        }

        // Uses the index from the XML document to get these.
        String temperature = doc
                .getFirstChild()
                .getChildNodes()
                .item(11)
                .getChildNodes()
                .item(11)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        String high = doc
                .getFirstChild()
                .getChildNodes()
                .item(13)
                .getChildNodes()
                .item(5)
                .getChildNodes()
                .item(3)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        String low = doc
                .getFirstChild()
                .getChildNodes()
                .item(13)
                .getChildNodes()
                .item(5)
                .getChildNodes()
                .item(5)
                .getChildNodes()
                .item(0)
                .getNodeValue();

        StationResult stationResult = new StationResult();
        stationResult.temperature = Double.parseDouble(temperature);
        stationResult.high = Double.parseDouble(high);
        stationResult.low = Double.parseDouble(low);
        stationResult.stationID = stationID;
        stationResult.provinceCode = provinceCode;

        if (Config.useFakeTemperature) {
            // Use fake data to simulate prime tapping conditions.
            stationResult.temperature = -0.0;
            stationResult.high = 10.0;
            stationResult.low = -10.0;
        }

        Config.stationResult = stationResult;

        return stationResult;
    }
}