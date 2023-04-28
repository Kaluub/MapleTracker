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
import com.ocdsb.mapletracker.data.StationResult;
import com.ocdsb.mapletracker.databinding.FragmentHomeBinding;
import com.ocdsb.mapletracker.interfaces.StationResultCallback;

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
                Config.useFakeTemperature = !Config.useFakeTemperature;
                Config.stationResult = null;
                this.updateWeatherElements();
                Snackbar.make(
                        view,
                        String.format("Fake temperature mode is now set to %b.", Config.useFakeTemperature),
                        Snackbar.LENGTH_SHORT
                ).show();
            });
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.updateWeatherElements();
    }

    public void updateFromStationResults(StationResult stationResults) {
        // Prevent crashing if user switches the fragment too early.
        if (binding == null) return;
        final TextView temperatureText = binding.temperature;
        final TextView splashText = binding.splash;
        // Format the temperature string properly.
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

    public void updateWeatherElements() {
        binding.temperature.setText(R.string.temperature_default);
        binding.splash.setText(R.string.splash_default);
        this.fetchStationResults(stationResults -> requireActivity().runOnUiThread(() -> updateFromStationResults(stationResults)));
    }

    private void fetchStationResults(StationResultCallback callback) {
        // Use a new thread to run the function in the background.
        new Thread(() -> {
            try {
                String[] stationDetails = Config.weatherAPI.getClosestStationDetails();
                callback.onResult(Config.weatherAPI.getStation(stationDetails[0], stationDetails[1]));
            } catch (Exception e) {
                System.out.println("Exception while fetching the weather.");
                System.out.println(e);
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}