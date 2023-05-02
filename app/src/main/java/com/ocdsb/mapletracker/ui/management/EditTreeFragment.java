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

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.data.TreePin;
import com.ocdsb.mapletracker.databinding.FragmentEditTreeBinding;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Arrays;

public class EditTreeFragment extends Fragment {
    private FragmentEditTreeBinding binding;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentEditTreeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //Add the map to the fragment
        //Building the map
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        MapAPI mapAPI = new MapAPI();
        map = root.findViewById(R.id.map);

        //Request Permissions necessary for map to function.
        String [] Permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionsIfNecessary(Permissions);
        map = mapAPI.buildMap(map, getContext());
        map.getController().setCenter(new GeoPoint(Config.locationAPI.latitude, Config.locationAPI.longitude));
        //Get user text input
        EditText editText = (EditText) root.findViewById(R.id.editName);
        System.out.println("This is edit text: " + editText);
        System.out.println(editText.getText());
        MaterialButton button = binding.saveButton;
        System.out.println(button);
        button.setOnClickListener(v -> {
            System.out.println("The rad user tapped the cool button");
            System.out.println(editText.getText());
        });

        //Adding the spinner to the fragment
        Spinner spinner = root.findViewById(R.id.tree_spinner);
        //CharSequence Array which the spinner will display to the user
        ArrayList<String> name = new ArrayList<>();
        mapAPI.loadPins();
        for (TreePin pin : mapAPI.treePins) {
            name.add(pin.name);
        }
        System.out.println(name);
        CharSequence[] namer ={"this","thing","that"};
        //Initialise the spinner
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(name);
        //Specify layout used by the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply adapter to the spinner
        spinner.setAdapter(adapter);
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