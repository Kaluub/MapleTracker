package com.example.maplelogger.API;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherAPI {
    public WeatherAPI() {
    }

    public String getClosestStationID() {
        return "s0000430";
    }

    public double getStationTemperature(String stationID) {
        Document doc = getXMLFromURL("https://dd.weather.gc.ca/citypage_weather/xml/ON/s0000430_e.xml");
        String temperature = doc.getFirstChild().getChildNodes().item(11).getChildNodes().item(11).getChildNodes().item(0).getNodeValue();
        return Double.parseDouble(temperature);
    }

    public Document getXMLFromURL(String address) {
        try {
            URL url = new URL(address);
            HttpURLConnection con;
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in;
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            StringBuilder content = new StringBuilder();
            while (true) {
                String inputLine = in.readLine();
                if (inputLine == null)
                    break;
                content.append(inputLine);
            }
            in.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new InputSource(new StringReader(String.valueOf(content))));
        } catch (Exception e) {
            return null;
        }
    }
}