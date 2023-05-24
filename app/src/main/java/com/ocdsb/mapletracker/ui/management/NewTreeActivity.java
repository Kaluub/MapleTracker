package com.ocdsb.mapletracker.ui.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Choreographer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.ActivityNewTreeBinding;
import com.ocdsb.mapletracker.databinding.FragmentNewTreeBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Arrays;

public class NewTreeActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tree);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Config.useGallons ){
            TextView units = findViewById(R.id.units_text);
            units.setText(getString(R.string.gallons));
        }

        // Request permissions necessary for map to function.
        String [] Permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        //requestPermissionsIfNecessary(Permissions);

        // Building the map.
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        MapAPI mapAPI = new MapAPI();
        map = findViewById(R.id.map);
        map = mapAPI.buildMap(map);

        for (TreePin pin : mapAPI.treePins) {
            Marker treeMarker = new Marker(map);
            treeMarker.setPosition(new GeoPoint(pin.latitude, pin.longitude));
            treeMarker.setTitle(pin.name);
            map.getOverlays().add(treeMarker);
        }

        Marker marker = new Marker(map);
        marker.setPosition((GeoPoint) map.getMapCenter());
        marker.setInfoWindow(null);
        map.getOverlays().add(marker);

        // This works, but it might be smarter to instead render the pin externally.
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long l) {
                marker.setPosition((GeoPoint) map.getMapCenter());
                Choreographer.getInstance().postFrameCallback(this);
            }
        });

        // Add user text input.
        EditText name = findViewById(R.id.addName);
        EditText sap = findViewById(R.id.add_collected);
        // Add save button.
        MaterialButton button = findViewById(R.id.save_button2);
        button.setOnClickListener(v -> {
            TreePin treePin = new TreePin();
            treePin.name = name.getText().toString();
            double litres = Double.parseDouble(sap.getText().toString());
            if (Config.useGallons) {
                litres *= 3.785;
            }
            treePin.sapLitresCollectedTotal = litres;
            treePin.sapLitresCollectedResettable = litres;
            treePin.latitude = map.getMapCenter().getLatitude();
            treePin.longitude = map.getMapCenter().getLongitude();
            mapAPI.treePins.add(treePin);
            mapAPI.savePins();
            // Navigate back to main menu.
            Snackbar saveSnackBar = Snackbar.make(getWindow().getDecorView().getRootView(), "Saved your new tree.", Snackbar.LENGTH_SHORT);
            saveSnackBar.show();
            finish();
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        // This will refresh the osmdroid configuration on resuming.
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}