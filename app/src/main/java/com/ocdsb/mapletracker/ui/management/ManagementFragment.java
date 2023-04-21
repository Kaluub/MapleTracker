package com.ocdsb.mapletracker.ui.management;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.button.MaterialButton;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.MapAPI;
import com.ocdsb.mapletracker.databinding.FragmentManagementBinding;
import com.ocdsb.mapletracker.ui.home.HomeFragment;

public class ManagementFragment extends Fragment {

    private FragmentManagementBinding binding;
   // private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
   // private MapView map = null;
    //private final ArrayList<GeoPoint> lookup = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ManagementViewModel managementViewModel =
                new ViewModelProvider(this).get(ManagementViewModel.class);

        binding = FragmentManagementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MaterialButton button = binding.newTreeButton;
        button.setOnClickListener(v -> {

            NavHostFragment.findNavController(ManagementFragment.this).navigate(R.id.navigation_new_tree);
            Log.d("BUTTONS", "User tapped the New Tree Button");
        });
        MaterialButton button2 = binding.editTreeButton;
        button2.setOnClickListener(v -> {

            NavHostFragment.findNavController(ManagementFragment.this).navigate(R.id.navigation_edit_tree);
            Log.d("BUTTONS", "User tapped the Edit Tree Button");
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
       // map.onPause();
    }

    /*@Override
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
    } */
}
