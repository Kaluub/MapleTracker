package com.example.maplelogger.Web;

import org.w3c.dom.Document;
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

public class API {
    public API() {
    }

    public double GetStationTemperature(String stationID) {
        stationID.chars();
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
        System.out.println(doc.getFirstChild().getNodeName());
        System.out.println(doc.getFirstChild().getChildNodes().item(6).getNodeName());
        NodeList nodeList = doc.getFirstChild().getChildNodes();
        for (int j = 0; j < nodeList.getLength(); j++) {
            Node node = nodeList.item(j);
            System.out.println(node.getNodeName() + ": index " + j);
        }
        return 4.0;
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