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

    public MapView buildMap (MapView m){
        map = m;
        map.setTileSource(TileSourceFactory.MAPNIK);
        // Giving the user the ability to zoom the map
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setScrollableAreaLimitLatitude(85, -85, 0);
        map.setMultiTouchControls(true);
        map.setMinZoomLevel(2.0);
        // Changing the default map location and zoom
        IMapController mapController = map.getController();
        mapController.setZoom(19.0);
        mapController.setCenter(new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude));
        loadPins();
        return map;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return true;
    }

    public void loadPins() {
        treePins.clear();
        String store = Config.fileManager.readFile(map.getContext(), Config.fileName);
        if (store == null) {
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
        return true;
    }
}
