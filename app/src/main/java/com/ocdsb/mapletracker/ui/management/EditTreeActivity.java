package com.ocdsb.mapletracker.ui.management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import java.util.Date;
import java.util.Objects;

public class EditTreeActivity extends AppCompatActivity {
    private MapView map = null;
    private final MapAPI mapAPI = new MapAPI();
    private TreePin pin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tree);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = findViewById(R.id.map);

        if (Config.useGallons) {
            TextView units = findViewById(R.id.units_text);
            units.setText(getString(R.string.gallons));
        }

        // Build map.
        map = mapAPI.buildMap(map);
        map.getController().setCenter(new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude));

        AutoCompleteTextView treeDropDown = findViewById(R.id.tree_dropDown);
        //Create array list to store the name of all trees
        ArrayList<CharSequence> names = new ArrayList<>();
        // Add each tree to the map & add it's name to the list of tree names
        int n = 0;
        for (TreePin tPin : mapAPI.treePins) {
            names.add(tPin.name);
            Marker treeMarker = new Marker(map);
            treeMarker.setPosition(new GeoPoint(tPin.latitude, tPin.longitude));
            treeMarker.setTitle(tPin.name);
            int index = n;
            treeMarker.setOnMarkerClickListener((marker, mapView) -> {
                treeDropDown.setListSelection(index);
                System.out.println(index);
                EditText treeName = findViewById(R.id.editName);
                treeName.setText(tPin.name);
                map.getController().animateTo(treeMarker.getPosition());
                map.getController().setZoom(18.0);
                return false;
            });
            map.getOverlays().add(treeMarker);
            n += 1;
        }

        // Adds the tree names to the dropdown
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, names);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        treeDropDown.setAdapter(adapter);

        treeDropDown.setOnItemClickListener((adapterView, view, i, l) -> {
            pin = mapAPI.treePins.get(i);
            EditText treeName = findViewById(R.id.editName);
            treeName.setText(pin.name);
            GeoPoint p = new GeoPoint(pin.latitude, pin.longitude);
            map.getController().animateTo(p);
            map.getController().setZoom(18.0);
        });

        // Add save button.
        MaterialButton button = findViewById(R.id.save_button);
        button.setOnClickListener(view -> {
            if (pin == null) {
                Snackbar noSelected = Snackbar.make(getWindow().getDecorView().getRootView(), R.string.none_selected, Snackbar.LENGTH_SHORT);
                View noSelectedView = noSelected.getView();
                noSelectedView.setTranslationY(-(convertDpToPixel(48, this)));
                noSelected.show();
                return;
            }
            // Get EditText elements.
            EditText treeName = findViewById(R.id.editName);
            EditText sapChange = findViewById(R.id.edit_collected);
            // Update the pin's variables.
            pin.name = treeName.getText().toString();
            double increment = Double.parseDouble(sapChange.getText().toString());
            if (Config.useGallons) {
                increment *= 3.785;
            }
            pin.sapLitresCollectedTotal += increment;
            pin.sapLitresCollectedResettable += increment;
            pin.editedAt = new Date();
            pin.editsTotal += 1;
            pin.editsResettable += 1;
            // Save pins.
            mapAPI.savePins();
            Snackbar saveSnackbar = Snackbar.make(getWindow().getDecorView().getRootView(),R.string.changes_saved, Snackbar.LENGTH_SHORT);
            View saveSnackBarView = saveSnackbar.getView();
            saveSnackBarView.setTranslationY(-(convertDpToPixel(48, this)));
            saveSnackbar.show();
            finish();
        });
    }

    public float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}