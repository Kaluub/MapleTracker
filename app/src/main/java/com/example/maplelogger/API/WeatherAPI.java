package com.example.maplelogger.API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Document;

public class WeatherAPI {
    Parser parser;
    LocationAPI locationAPI;

    public WeatherAPI() {
        parser = new Parser();
        locationAPI = new LocationAPI();
    }

    public String getClosestStationID() {
        JsonObject webData = parser.getJSONfromURL("https://collaboration.cmc.ec.gc.ca/cmc/cmos/public_doc/msc-data/citypage-weather/site_list_en.geojson");
        JsonArray featuresArray = webData.getAsJsonArray("features");
        String bestFeatureId = "s0000430";
        double closestDistance = 100000;
        for (JsonElement element : featuresArray) {
            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            double elementLatitude = properties.get("Latitude").getAsDouble();
            double elementLongitude = properties.get("Longitude").getAsDouble();

            double distance = Math.sqrt(Math.pow(locationAPI.latitude - elementLatitude, 2) + Math.pow(locationAPI.longitude - elementLongitude, 2));
            if (distance < closestDistance) {
                bestFeatureId = properties.get("Codes").getAsString();
                closestDistance = distance;
            }
        }

        System.out.println(bestFeatureId);
        System.out.println(closestDistance);

        return bestFeatureId;
    }

    public double getStationTemperature(String stationID) {
        // Uses the station ID to get the weather from that station.
        Document doc = parser.getXMLFromURL(String.format("https://dd.weather.gc.ca/citypage_weather/xml/ON/%s_e.xml", stationID));
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
}