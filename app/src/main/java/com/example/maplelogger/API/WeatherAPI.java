package com.example.maplelogger.API;

import org.w3c.dom.Document;

public class WeatherAPI {
    Parser parser;
    public WeatherAPI() {
        parser = new Parser();
    }

    public String getClosestStationID() {
        // TODO: Use the station location endpoint instead of hardcoding Ottawa.
        // This requires a (GEO)JSON parser.
        // URL: https://collaboration.cmc.ec.gc.ca/cmc/cmos/public_doc/msc-data/citypage-weather/site_list_en.geojson
        return "s0000430";
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