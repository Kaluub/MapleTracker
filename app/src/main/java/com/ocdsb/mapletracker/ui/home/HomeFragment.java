package com.ocdsb.mapletracker.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.api.StationResult;
import com.ocdsb.mapletracker.databinding.FragmentHomeBinding;

import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final Random rng = new Random();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        if (Config.debugMode) {
            final Button debugButton = binding.debug;
            debugButton.setVisibility(View.VISIBLE);
            debugButton.setOnClickListener(view -> {
                String[] stationDetails = Config.weatherAPI.getClosestStationDetails();
                StationResult stationResult = Config.weatherAPI.getStation(stationDetails[0], stationDetails[1]);
                Snackbar.make(view, "Temperature right now is " + stationResult.temperature + " at station ID " + stationDetails[0] + " (" + stationDetails[1] + ")", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            });
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.fetchStationResults(stationResults -> requireActivity().runOnUiThread(() -> updateFromStationResults(stationResults)));
    }

    public void updateFromStationResults(StationResult stationResults) {
        final TextView temperatureText = binding.temperature;
        final TextView splashText = binding.splash;
        temperatureText.setText(String.format(getResources().getString(R.string.temperature_replace), stationResults.temperature));
        if (stationResults.low < 0 && stationResults.high > 0) {
            // The weather is good for maple tapping. Use a good splash text.
            String[] splashGood = getResources().getStringArray(R.array.splash_good);
            int splashIndex = rng.nextInt(splashGood.length);
            splashText.setText(splashGood[splashIndex]);
        } else {
            // The weather is not good for maple tapping. Use a bad splash text.
            String[] splashBad = getResources().getStringArray(R.array.splash_bad);
            int splashIndex = rng.nextInt(splashBad.length);
            splashText.setText(splashBad[splashIndex]);
        }
    }

    private void fetchStationResults(StationResultCallback callback) {
        new Thread(() -> {
            String[] stationDetails = Config.weatherAPI.getClosestStationDetails();
            callback.onResult(Config.weatherAPI.getStation(stationDetails[0], stationDetails[1]));
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

interface StationResultCallback {
    void onResult(StationResult stationResult);
}