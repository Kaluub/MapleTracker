package com.ocdsb.mapletracker.api;


import android.content.Context;

import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.data.TreePin;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MapAPI implements MapEventsReceiver {
    private MapView map = null;
    public final ArrayList<TreePin> treePins = new ArrayList<>();

    public MapView buildMap (MapView m) {
        // Apply global modifications to the map.
        map = m;
        map.setTileSource(TileSourceFactory.MAPNIK);
        // Give the user the ability to zoom the map.
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        // Limit the map to the bounds of the planet.
        map.setScrollableAreaLimitLatitude(85, -85, 0);
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(2.0);
        // Changing the default map location and zoom
        IMapController mapController = map.getController();
        mapController.setZoom(19.0);
        mapController.setCenter(new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude));
        // This function fills in the "treePins" ArrayList but does not actually render the pins.
        loadPins();
        return map;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return true;
    }

    public void loadPins() {
        // Fill the "treePins" ArrayList with all of the pins in the selected farm.
        treePins.clear();
        String store = Config.fileManager.readFile(map.getContext(), Config.fileName);
        if (store == null) {
            return;
        }
        for (String treeData : store.split("\n")) {
            if (treeData.length() <= 0) {
                continue;
            }
            TreePin pin = TreePin.getFromFileLine(treeData);
            treePins.add(pin);
        }
    }

    public void loadPins(Context context) {
        // Used in situations where there is no map loaded, such as in the Management fragment.
        treePins.clear();
        String store = Config.fileManager.readFile(context, Config.fileName);
        if (store == null) {
            return;
        }
        for (String treeData : store.split("\n")) {
            if (treeData.length() <= 0) {
                continue;
            }
            TreePin pin = TreePin.getFromFileLine(treeData);
            treePins.add(pin);
        }
    }

    public void savePins() {
        // Convert local "treePins" object to the file format & save.
        StringBuilder store = new StringBuilder();
        for (TreePin pin : this.treePins) {
            store.append("\n").append(pin.saveToLine());
        }
        Config.fileManager.saveFile(map.getContext(), Config.fileName, store.toString());
    }

    public void savePins(Context context) {
        // For contexts without a map on screen.
        StringBuilder store = new StringBuilder();
        for (TreePin pin : this.treePins) {
            store.append("\n").append(pin.saveToLine());
        }
        Config.fileManager.saveFile(context, Config.fileName, store.toString());
    }


    @Override
    public boolean longPressHelper(GeoPoint p) {
        // Prevent default action.
        return true;
    }
}
