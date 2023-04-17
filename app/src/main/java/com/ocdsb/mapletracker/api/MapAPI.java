package com.ocdsb.mapletracker.api;

import com.ocdsb.mapletracker.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapAPI implements MapEventsReceiver {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private GeoPoint[] lookup = new GeoPoint[100];

    public MapView buildMap (int m){

        //map = (MapView) root.findViewById(R.id.map);
        //map.setTileSource(TileSourceFactory.MAPNIK);
        return null;
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        IMapController mapController = map.getController();
        mapController.animateTo(p);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(p);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);
        System.out.println(p);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }

    public boolean isFree(GeoPoint p){
        for (GeoPoint geoPoint : lookup) {
            if (geoPoint == p) {
                return false;
            }
        }
        return true;
    }
    public int lookupEmpty(){
        for (int i = 0; i< lookup.length;i++){
            if (lookup[i]== null){
                return i;
            }
        }
        return -1;
    }
}
