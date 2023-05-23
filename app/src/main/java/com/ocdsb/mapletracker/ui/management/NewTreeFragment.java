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
import android.view.Choreographer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentNewTreeBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Arrays;

public class NewTreeFragment extends Fragment {
    private FragmentNewTreeBinding binding;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNewTreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (Config.useGallons) {
            binding.unitsText.setText(getString(R.string.gallons));
        }

        // Request permissions necessary for map to function.
        String [] Permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        requestPermissionsIfNecessary(Permissions);

        // Building the map.
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        MapAPI mapAPI = new MapAPI();
        map = root.findViewById(R.id.map);
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
        EditText name = root.findViewById(R.id.addName);
        EditText sap = root.findViewById(R.id.add_collected);
        // Add save button.
        MaterialButton button = binding.saveButton2;
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
            Snackbar saveSnackBar = Snackbar.make(root, "Saved your new tree.", Snackbar.LENGTH_SHORT);
            saveSnackBar.show();
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