package com.ocdsb.mapletracker.ui.management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Choreographer;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Arrays;

public class NewTreeActivity extends AppCompatActivity {
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
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", true);
            setResult(NewTreeActivity.RESULT_OK,returnIntent);
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
    public boolean onSupportNavigateUp(){
        setResult(NewTreeActivity.RESULT_CANCELED);
        finish();
        return true;
    }
}