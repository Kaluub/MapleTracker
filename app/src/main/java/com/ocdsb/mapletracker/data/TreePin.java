package com.ocdsb.mapletracker.data;

import com.ocdsb.mapletracker.Config;

import java.util.Date;

public class TreePin {
    // Represents a tree pin & it's respective data.
    public Date createdAt = new Date();
    public Date editedAt = new Date();
    public String name;
    public double latitude = 0;
    public double longitude = 0;
    public double sapLitresCollectedTotal = 0;
    public double sapLitresCollectedResettable = 0;
    public int editsTotal = 0;
    public int editsResettable = 0;

    public String saveToLine() {
        return createdAt +
                Config.fileSeparator +
                editedAt +
                Config.fileSeparator +
                name +
                Config.fileSeparator +
                latitude +
                Config.fileSeparator +
                longitude +
                Config.fileSeparator +
                sapLitresCollectedTotal +
                Config.fileSeparator +
                sapLitresCollectedResettable +
                Config.fileSeparator +
                editsTotal +
                Config.fileSeparator +
                editsResettable;
    }

    public static TreePin getFromFileLine(String line) {
        String[] data = line.split(",");
        TreePin pin = new TreePin();
        pin.createdAt = data.length > 0 ? new Date(Integer.parseInt(data[0])) : new Date();
        pin.editedAt = data.length > 1 ? new Date(Integer.parseInt(data[1])) : new Date();
        pin.name = data.length > 2 ? data[2] : "Default";
        pin.latitude = data.length > 3 ? Double.parseDouble(data[3]): 0.0;
        pin.longitude = data.length > 4 ? Double.parseDouble(data[4]) : 0.0;
        pin.sapLitresCollectedTotal = data.length > 5 ? Double.parseDouble(data[5]) : 0.0;
        pin.sapLitresCollectedResettable = data.length > 6 ? Double.parseDouble(data[6]) : 0.0;
        pin.editsTotal = data.length > 7 ? Integer.parseInt(data[7]) : 0;
        pin.editsResettable = data.length > 8 ? Integer.parseInt(data[8]) : 0;
        return pin;
    }
}
