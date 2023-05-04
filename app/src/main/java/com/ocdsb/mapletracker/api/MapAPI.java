package com.ocdsb.mapletracker.api;


import android.content.Context;
import android.graphics.Rect;

import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.data.TreePin;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class MapAPI implements MapEventsReceiver {
    private MapView map = null;
    public ArrayList<TreePin> treePins = new ArrayList<>();

    public MapView buildMap (MapView m, Context c){
        map = m;
        map.setTileSource(TileSourceFactory.MAPNIK);
        // Giving the user the ability to zoom the map
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setScrollableAreaLimitLatitude(85, -85, 0);
        map.setMultiTouchControls(true);
        // Changing the default map location and zoom
        IMapController mapController = map.getController();
        mapController.setZoom(10.0);
        loadPins();
        return map;
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        IMapController mapController = map.getController();
        mapController.animateTo(p);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(p);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);
        savePins();
        return true;
    }

    public void loadPins() {
        treePins.clear();
        String store = Config.fileManager.readFile(map.getContext(), Config.fileName);
        if (store == null) {
            System.out.println("Pins is null");
            return;
        }
        for (String treeData : store.split("\n")) {
            if (treeData.length() <= 0) {
                continue;
            }
            try {
                TreePin pin = TreePin.getFromFileLine(treeData);
                treePins.add(pin);
            } catch (Exception e) {
                // The tree could not be read.
            }
        }
    }

    public void loadPins(Context context) {
        // Used in situations where the map is not loaded (ie. statistics fragment).
        treePins.clear();
        String store = Config.fileManager.readFile(context, Config.fileName);
        if (store == null) {
            System.out.println("Pins is null");
            return;
        }
        for (String treeData : store.split("\n")) {
            if (treeData.length() <= 0) {
                continue;
            }
            try {
                TreePin pin = TreePin.getFromFileLine(treeData);
                treePins.add(pin);
            } catch (Exception e) {
                // The tree could not be read.
            }
        }
    }

    public void savePins() {
        StringBuilder store = new StringBuilder();
        for (TreePin pin : this.treePins) {
            store.append("\n").append(pin.saveToLine());
        }
        Config.fileManager.saveFile(map.getContext(), Config.fileName, store.toString());
    }

    public void savePins(Context context) {
        StringBuilder store = new StringBuilder();
        for (TreePin pin : this.treePins) {
            store.append("\n").append(pin.saveToLine());
        }
        Config.fileManager.saveFile(context, Config.fileName, store.toString());
    }


    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}
