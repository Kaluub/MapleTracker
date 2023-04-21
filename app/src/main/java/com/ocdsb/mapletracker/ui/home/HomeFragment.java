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
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.StationResult;
import com.ocdsb.mapletracker.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        if (Config.debugMode) {
            final Button debugButton = binding.debug;
            debugButton.setVisibility(View.VISIBLE);
            debugButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String[] stationDetails = Config.weatherAPI.getClosestStationDetails();
                    StationResult stationResult = Config.weatherAPI.getStation(stationDetails[0], stationDetails[1]);
                    Snackbar.make(view, "Temperature right now is " + stationResult.temperature + " at station ID " + stationDetails[0] + " (" + stationDetails[1] + ")", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        final TextView temperatureText = binding.temperature;
        double fetchedTemperature = this.fetchTemperature();
        temperatureText.setText(String.format(getResources().getString(R.string.temperature_replace), fetchedTemperature));
    }

    public double fetchTemperature() {
        String[] stationDetails = Config.weatherAPI.getClosestStationDetails();
        StationResult stationResult = Config.weatherAPI.getStation(stationDetails[0], stationDetails[1]);
        return stationResult.temperature;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}