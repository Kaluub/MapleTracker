package com.example.maplelogger.API;

import com.google.gson.JsonParser;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Parser {
    public Parser() {
    }

    public Document getXMLFromURL(String address) {
        // Parses an XML file online to a Document.
        try {
            // Open the connection.
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Read.
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );

            StringBuilder content = new StringBuilder();
            while (true) {
                String inputLine = reader.readLine();
                if (inputLine == null)
                    break;
                content.append(inputLine);
            }
            reader.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse and return a Document.
            return builder.parse(
                    new InputSource(
                            new StringReader(String.valueOf(content))
                    )
            );
        } catch (IOException | SAXException | ParserConfigurationException e) {
            // In case of any exceptions, we have nothing to return.
            return null;
        }
    }

    public Object getJSONfromURL(String address) {
        try {
            // Open the connection.
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Read.
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );

            // Parse JSON.
            JsonParser.parseReader(reader).getAsJsonObject();
            return null;
        } catch (IOException e) {
            // In case of any exceptions, we have nothing to return.
            return null;
        }
    }
}
