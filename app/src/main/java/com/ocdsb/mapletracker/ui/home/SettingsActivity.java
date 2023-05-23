package com.ocdsb.mapletracker.ui.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.ocdsb.mapletracker.Config;
import com.ocdsb.mapletracker.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Define saving behaviour here. You can use the same thing for all switches.
        SwitchCompat useGallonsSwitch = findViewById(R.id.use_gallons);
        useGallonsSwitch.setChecked(Config.useGallons);
        useGallonsSwitch.setOnCheckedChangeListener((switchView, checked) -> {
            Config.useGallons = checked;
            Config.saveConfig(this);
            Config.stationResult = null;
        });

        SwitchCompat useFahrenheitSwitch = findViewById(R.id.use_fahrenheit);
        useFahrenheitSwitch.setChecked(Config.useFahrenheit);
        useFahrenheitSwitch.setOnCheckedChangeListener((switchView, checked) -> {
            Config.useFahrenheit = checked;
            Config.saveConfig(this);
        });

        SwitchCompat useDebugModeSwitch = findViewById(R.id.use_debug_mode);
        useDebugModeSwitch.setChecked(Config.debugMode);
        useDebugModeSwitch.setOnCheckedChangeListener((switchView, checked) -> {
            Config.debugMode = checked;
            Config.saveConfig(this);
        });
    }
}