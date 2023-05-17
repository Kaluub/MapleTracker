package com.ocdsb.mapletracker.ui.home;

import android.os.Bundle;
import android.view.View;

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

        SwitchCompat useGallonsSwitch = findViewById(R.id.use_gallons);
        useGallonsSwitch.setChecked(Config.useGallons);
        useGallonsSwitch.setOnCheckedChangeListener((switchView, checked) -> {
            Config.useGallons = checked;
            Config.saveConfig(this);

            System.out.println("useGallons set to " + checked);
        });
    }
}