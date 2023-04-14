package com.ocdsb.mapletracker.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Document;

public class WeatherAPI {
    Parser parser;
    LocationAPI locationAPI;

    public WeatherAPI() {
        parser = new Parser();
    }

    public double getDistance(JsonObject properties) {
        double elementLatitude = properties.get("Latitude").getAsDouble();
        double elementLongitude = properties.get("Longitude").getAsDouble();
        return Math.sqrt(Math.pow(locationAPI.latitude - elementLatitude, 2) + Math.pow(locationAPI.longitude - elementLongitude, 2));
    }

    public String[] getClosestStationDetails() {
        JsonObject webData = parser.getJSONfromURL("https://collaboration.cmc.ec.gc.ca/cmc/cmos/public_doc/msc-data/citypage-weather/site_list_en.geojson");
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

    public double getStationTemperature(String stationID, String provinceCode) {
        // Uses the station ID to get the weather from that station.
        Document doc = parser.getXMLFromURL(String.format("https://dd.weather.gc.ca/citypage_weather/xml/%s/%s_e.xml", provinceCode, stationID));
        if (doc == null) {
            // If there is no available Document, return 0.
            return 0;
        }

        // I wish I knew a better way to do this.
        String temperature = doc
                .getFirstChild()
                .getChildNodes()
                .item(11)
                .getChildNodes()
                .item(11)
                .getChildNodes()
                .item(0)
                .getNodeValue();
        // Node value is a string, but we can trust that it will always be a number.
        return Double.parseDouble(temperature);
    }

    public void updateLocationAPI(LocationAPI api) {
        locationAPI = api;
    }
}