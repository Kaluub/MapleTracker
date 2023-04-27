package com.ocdsb.mapletracker.api;


import android.content.Context;
import com.ocdsb.mapletracker.Config;
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
    public ArrayList<GeoPoint> lookup = new ArrayList<>();

    public MapView buildMap (MapView m, Context c){
        map = m;
        map.setTileSource(TileSourceFactory.MAPNIK);
        //Giving the user the ability to zoom the map
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);
        //Changing the default map location and zoom
        IMapController mapController = map.getController();
        mapController.setZoom(10.0);
        //mapController.setCenter(new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude));
        loadPins();
        //MapView mMapView = new MapView(inflater.getContext());
        //allow user to add pins -- this should probably be moved to the fragment where it is used
        /*MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if(lookup.contains(p)) {
                    System.out.println(p + " already has a pin");
                    return false;
                }
                Marker startMarker = new Marker(map);
                startMarker.setPosition(p);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                map.getOverlays().add(startMarker);
                lookup.add(p);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }; */


        //MapEventsOverlay OverlayEvents = new MapEventsOverlay(c, mReceive);
        //map.getOverlays().add(OverlayEvents);


        //map.setTileSource(TileSourceFactory.MAPNIK);
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
        String store = Config.fileManager.readFile(map.getContext(), "pins");
        if (store == null) {
            System.out.println("Pins is null");
            return;
        }
        for (String treeData : store.split("\n")) {
            if (treeData.length() <= 0) {
                continue;
            }
            String[] data = treeData.split(",");
            GeoPoint geoPoint = new GeoPoint(Double.parseDouble(data[0]), Double.parseDouble(data[1]));
            Marker marker = new Marker(map);
            marker.setPosition(geoPoint);
            map.getOverlays().add(marker);
        }
    }

    public void savePins() {
        System.out.println("Store Pins Method has been called");
        StringBuilder store = new StringBuilder();
        for (GeoPoint point : this.lookup) {
            store.append("\n")
                    .append(point.getLatitude())
                    .append(",")
                    .append(point.getLongitude());
        }
        Config.fileManager.saveFile(map.getContext(), "pins", store.toString());
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

}
