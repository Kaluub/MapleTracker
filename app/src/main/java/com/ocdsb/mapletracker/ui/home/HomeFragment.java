package com.ocdsb.mapletracker.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;
import com.ocdsb.mapletracker.data.StationResult;
import com.ocdsb.mapletracker.databinding.FragmentHomeBinding;
import com.ocdsb.mapletracker.interfaces.StationResultCallback;

import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final Random rng = new Random();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        binding.dateText.setText(DateFormat.getDateInstance().format(new Date()));

        binding.settingsButton.setOnClickListener(view -> startActivity(new Intent(requireContext(), SettingsActivity.class)));

        if (Config.debugMode) {
            // Adds debug options if debug mode is enabled
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
    public void onResume() {
        super.onResume();
        this.updateWeatherElements();
    }

    public void updateFromStationResults(StationResult stationResults) {
        // Prevent crashing if user switches the fragment too early.
        if (binding == null) return;
        // If permissions aren't enabled after fetch, don't use them!
        if (Config.permissionsDisabled) {
            binding.temperature.setText(R.string.permission_error);
            binding.splash.setText("");
            binding.forecastHeader.setText("");
            return;
        }

        // Initialises text views to display information to the users
        final TextView temperatureText = binding.temperature;
        final TextView HighText = binding.todayHigh;
        final TextView LowText = binding.todayLow;
        final TextView splashText = binding.splash;
        // Format the temperature string properly.
        String units = getString(R.string.temperature_replace);
        double temperature = stationResults.temperature;
        double high = stationResults.high;
        double low = stationResults.low;
        if (Config.useFahrenheit) {
            units = getString(R.string.temperature_replace_fahrenheit);
            temperature = 1.8 * temperature + 32;
            high = 1.8 * high + 32;
            low = 1.8 * low + 32;
        }
        temperatureText.setText(String.format(units, temperature));
        HighText.setText((String.format(units, high)));
        LowText.setText((String.format(units,low)));
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

        // Adding weather icon for the current conditions
        if (stationResults.weatherIcon != null) {
            updateWeatherIcon(R.id.weather_icon, stationResults.weatherIcon);
        }

        // Create variables to store the high temperature for the next three days
        double high1 = stationResults.forecastHighs.get(0);
        double high2 = stationResults.forecastHighs.get(1);
        double high3 = stationResults.forecastHighs.get(2);

        // Create variables to store the icon codes for the next tree days
        String highIcon1 = stationResults.iconHighs.get(0);
        String highIcon2 = stationResults.iconHighs.get(1);
        String highIcon3 = stationResults.iconHighs.get(2);

        // Create variables to store the low temperature for the next three highs
        double low1 = stationResults.forecastLows.get(0);
        double low2 = stationResults.forecastLows.get(1);
        double low3 = stationResults.forecastLows.get(2);

        // Create variables to store the icon codes for the next three lows
        String lowIcon1 = stationResults.iconLows.get(0);
        String lowIcon2 = stationResults.iconLows.get(1);
        String lowIcon3 = stationResults.iconLows.get(2);

        // Display icons for the next three days & nights
        if (stationResults.weatherIcon != null) {
            updateWeatherIcon(R.id.high_icon_one, highIcon1);
            updateWeatherIcon(R.id.high_icon_two, highIcon2);
            updateWeatherIcon(R.id.high_icon_three, highIcon3);

            updateWeatherIcon(R.id.low_icon_one, lowIcon1);
            updateWeatherIcon(R.id.low_icon_two, lowIcon2);
            updateWeatherIcon(R.id.low_icon_three, lowIcon3);
        }

        if (Config.useFahrenheit) {
            // Converts temperatures from Celsius to Fahrenheit if the user has enable use Fahrenheit
            high1 = 1.8 * high1 + 32;
            high2 = 1.8 * high2 + 32;
            high3 = 1.8 * high3 + 32;

            low1 = 1.8 * low1 + 32;
            low2 = 1.8 * low2 + 32;
            low3 = 1.8 * low3 + 32;
        }

        //Displaying the high and low temperature forecast to the user
        binding.highOne.setText(String.format("%s", String.format(units, high1)));
        binding.highTwo.setText(String.format("%s", String.format(units, high2)));
        binding.highThree.setText(String.format("%s", String.format(units, high3)));
        binding.lowOne.setText(String.format("%s", String.format(units, low1)));
        binding.lowTwo.setText(String.format("%s", String.format(units, low2)));
        binding.lowThree.setText(String.format("%s", String.format(units, low3)));
    }

    public void updateWeatherElements() {
        binding.temperature.setText(R.string.temperature_default);
        binding.splash.setText(R.string.splash_default);
        binding.todayHigh.setText(getString(R.string.temperature_default));
        binding.todayLow.setText(getString(R.string.temperature_default));
        if (Config.permissionsDisabled) {
            binding.temperature.setText(R.string.permission_error);
            binding.splash.setText("");
            binding.forecastHeader.setText("");
            return;
        }
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
                Log.e("ERROR", "Yikes!", e);
            }
        }).start();
    }

    private void updateWeatherIcon(int resId, String iconCode) {
        new Thread(() -> {
            try {
                String url = String.format("https://weather.gc.ca/weathericons/%s.gif", iconCode);
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
                if (binding != null) {
                    requireActivity().runOnUiThread(() -> {
                        ImageView imageView = requireView().findViewById(resId);
                        imageView.setImageBitmap(bitmap);
                    });
                }
            } catch (Exception e) {
                System.out.println("Exception while fetching a weather icon.");
                Log.e("ERROR", "Result:", e);
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}