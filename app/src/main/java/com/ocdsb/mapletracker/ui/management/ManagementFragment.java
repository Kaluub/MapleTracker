package com.ocdsb.mapletracker.ui.management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// imports required for OSM
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat; //unsure if needed
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.databinding.FragmentManagementBinding;

public class ManagementFragment extends Fragment implements MapEventsReceiver {

    private FragmentManagementBinding binding;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private ArrayList<GeoPoint> lookup = new ArrayList<GeoPoint>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManagementViewModel managementViewModel =
                new ViewModelProvider(this).get(ManagementViewModel.class);

        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDashboard;
       // dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getActivity().getApplicationContext(); //unsure how to fix error method should exist from android.content.Context
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string
        //inflate and create the map

        map = (MapView) root.findViewById(R.id.map);
        System.out.println(binding.getRoot());
        map.setTileSource(TileSourceFactory.MAPNIK);
        //Giving the user the ability to zoom the map
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);
        //Changing the default map location and zoom
        IMapController mapController = map.getController();
        mapController.setZoom(10);
        GeoPoint startPoint = new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude);
        mapController.setCenter(startPoint);
        //Request Permissions necessary for map to function
        String [] Permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionsIfNecessary(Permissions);
        MapView mMapView = new MapView(inflater.getContext());
        //Hard code a pin on the map
        GeoPoint mapPin = new GeoPoint(45.323171 ,-75.895422);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(mapPin);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        map.getOverlays().add(startMarker);
        lookup.add(mapPin);
        //GeoPoint pin2 = new GeoPoint(45.000,-75.000);
        //singleTapConfirmedHelper(pin2);

        //allow user to add pins
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                Toast.makeText(getContext(),p.getLatitude() + ", " + p.getLongitude(), Toast.LENGTH_SHORT).show();
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
        };


        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getContext(), mReceive);
        map.getOverlays().add(OverlayEvents);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
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
}
