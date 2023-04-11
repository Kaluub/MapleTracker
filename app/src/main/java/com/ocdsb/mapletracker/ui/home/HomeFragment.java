package com.ocdsb.mapletracker.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.MainActivity;
import com.ocdsb.mapletracker.api.LocationAPI;
import com.ocdsb.mapletracker.api.WeatherAPI;
import com.ocdsb.mapletracker.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private WeatherAPI weatherAPI;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final Button button = binding.button;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] stationDetails = weatherAPI.getClosestStationDetails();
                double temperature = weatherAPI.getStationTemperature(stationDetails[0], stationDetails[1]);
                Snackbar.make(view, "Temperature right now is " + temperature + " at station ID " + stationDetails[0] + " (" + stationDetails[1] + ")", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return root;
    }

    public void updateLocationAPI(LocationAPI locationAPI) {
        weatherAPI.updateLocationAPI(locationAPI);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}