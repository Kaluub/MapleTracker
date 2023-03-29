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

    public double GetStationTemperature(String stationID) {
        URL url;
        try {
            url = new URL("https://dd.weather.gc.ca/citypage_weather/xml/ON/s0000430_e.xml");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }

        BufferedReader in;
        try {
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String inputLine;
        StringBuilder content = new StringBuilder();
        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            content.append(inputLine);
        }

        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Document doc = convertStringToXMLDocument(String.valueOf(content));

        assert doc != null;
        NodeList nodes = doc.getFirstChild().getChildNodes().item(11).getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            NodeList nodeList = node.getChildNodes();
            System.out.println("(" + i + ") " + node.getNodeName() + ": " + node.getNodeValue());
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node jNode = nodeList.item(j);
                System.out.println("(" + j + ") " + jNode.getNodeName() + ": " + jNode.getNodeValue());
            }
        }

        String temperature = doc.getFirstChild().getChildNodes().item(11).getChildNodes().item(12).getNodeValue();
        System.out.println(temperature);
        return 4005.5;
        //return Double.parseDouble(temperature);
    }

    private String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }

    public Document convertStringToXMLDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}