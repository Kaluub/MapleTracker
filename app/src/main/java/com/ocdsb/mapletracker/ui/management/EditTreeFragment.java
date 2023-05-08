package com.ocdsb.mapletracker.ui.management;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentEditTreeBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EditTreeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentEditTreeBinding binding;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;
    private final MapAPI mapAPI = new MapAPI();
    private TreePin pin = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditTreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = root.findViewById(R.id.map);

        // Request Permissions necessary for map to function.
        String [] Permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionsIfNecessary(Permissions);

        // Build map.
        map = mapAPI.buildMap(map, getContext());
        map.getController().setCenter(new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude));

        // Load the spinner.
        Spinner spinner = root.findViewById(R.id.tree_spinner);
        ArrayList<String> names = new ArrayList<>();
        names.add("Select Tree");

        mapAPI.loadPins();
        for (TreePin pin : mapAPI.treePins) {
            names.add(pin.name);
            Marker marker = new Marker(map);
            marker.setPosition(new GeoPoint(pin.latitude, pin.longitude));
            marker.setTextIcon("T");
            map.getOverlays().add(marker);
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item);
        adapter.addAll(names);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        // Add save button.
        MaterialButton button = binding.saveButton;
        button.setOnClickListener(view -> {
            if (pin == null) {
                Snackbar.make(root, "You haven't selected a pin yet!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            // Get EditText elements.
            EditText treeName = binding.editName;
            EditText sapChange = binding.editCollected;
            // Update the pin's variables.
            pin.latitude = map.getMapCenter().getLatitude();
            pin.longitude = map.getMapCenter().getLongitude();
            pin.name = treeName.getText().toString();
            pin.sapLitresCollectedTotal += Double.parseDouble(sapChange.getText().toString());
            pin.sapLitresCollectedResettable += Double.parseDouble(sapChange.getText().toString());
            pin.editedAt = new Date();
            pin.editsTotal += 1;
            pin.editsResettable += 1;
            // Save pins.
            mapAPI.savePins();
            Snackbar saveSnackbar = Snackbar.make(root.findViewById(R.id.edit_tree_layout),"Changes Saved", Snackbar.LENGTH_SHORT);
            //saveSnackbar.setAnchorView(R.id.nav_view);
            saveSnackbar.show();
            //Snackbar.make(root, "Saved your changes.", Snackbar.LENGTH_SHORT).show();
            System.out.println(saveSnackbar.getAnchorView());
            NavHostFragment.findNavController(this).navigate(R.id.navigation_management);
        });
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
        // This will refresh the osmdroid configuration on resuming.
        map.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>(Arrays.asList(permissions).subList(0, grantResults.length));
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    // Method from implemented class, needed to get the spinner to work
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        if (pos != 0){
            pin = mapAPI.treePins.get(pos - 1);
            EditText treeName = binding.editName;
            treeName.setText(pin.name);
            GeoPoint p = new GeoPoint(pin.latitude,pin.longitude);
            map.getController().animateTo(p);
            map.getController().setZoom(18.0);
        } else Snackbar.make(getView(),"Please select a tree to edit.",Snackbar.LENGTH_SHORT).show();
    }

    // Method from implemented class, unsure of what to do with this
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}