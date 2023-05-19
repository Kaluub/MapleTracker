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
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

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

public class EditTreeFragment extends Fragment {
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
        map = mapAPI.buildMap(map);
        map.getController().setCenter(new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude));

        //Create array list to store the name of all trees
        ArrayList<CharSequence> names = new ArrayList<>();
        // Add each tree to the map & add it's name to the list of tree names
        for (TreePin tPin : mapAPI.treePins) {
            names.add(tPin.name);
            Marker treeMarker = new Marker(map);
            treeMarker.setPosition(new GeoPoint(tPin.latitude, tPin.longitude));
            treeMarker.setTitle(tPin.name);
            treeMarker.setOnMarkerClickListener((marker, mapView) -> {
                EditText treeName = binding.editName;
                treeName.setText(tPin.name);
                map.getController().animateTo(treeMarker.getPosition());
                map.getController().setZoom(18.0);
                return false;
            });
            map.getOverlays().add(treeMarker);
        }
        //Adds the tree names to the dropdown
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item,names);
        adapter.setDropDownViewResource(R.layout.dropdown_item);
        binding.treeDropDown.setAdapter(adapter);

        binding.treeDropDown.setOnItemClickListener((adapterView, view, i, l) -> {
            System.out.println("onItemClick where i = " + i);
            pin = mapAPI.treePins.get(i);
            EditText treeName = binding.editName;
            treeName.setText(pin.name);
            GeoPoint p = new GeoPoint(pin.latitude, pin.longitude);
            map.getController().animateTo(p);
            map.getController().setZoom(18.0);
        });

        // Add save button.
        MaterialButton button = binding.saveButton;
        button.setOnClickListener(view -> {
            if (pin == null) {
               Snackbar noSelected = Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(), "You haven't selected a pin yet!", Snackbar.LENGTH_SHORT);
               View  noSelectedView = noSelected.getView();
               noSelectedView.setTranslationY(-(convertDpToPixel(48,requireContext())));
               noSelected.show();
               System.out.println("pin is null");
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
            Snackbar saveSnackbar = Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),"Changes saved.", Snackbar.LENGTH_SHORT);
            View saveSnackBarView = saveSnackbar.getView();
            saveSnackBarView.setTranslationY(-(convertDpToPixel(48,requireContext())));
            saveSnackbar.show();
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

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}