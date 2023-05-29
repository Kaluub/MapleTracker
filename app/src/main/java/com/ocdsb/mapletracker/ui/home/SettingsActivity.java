package com.ocdsb.mapletracker.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import com.ocdsb.mapletracker.Config;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
        //Set an onClickListener to select a farm when the user selects a farm
        //Set an onLongClickListener to display a prompt confirming the user wishes to delete a farm
        AppCompatButton farm1 = findViewById(R.id.farm_1);
        farm1.setOnClickListener(view -> {
            Config.fileName ="pins";
            Config.saveConfig(this);
        });
        farm1.setOnLongClickListener(view -> {
            System.out.println("Farm one was long pressed");
            showAlert("pins");
            return true;
        });

        AppCompatButton farm2 = findViewById(R.id.farm_2);
        farm2.setOnClickListener(view -> {
            Config.fileName ="pins2";
            Config.saveConfig(this);
        });
        farm2.setOnLongClickListener(view -> {
            System.out.println("Farm one was long pressed");
            showAlert("pins2");
            return true;
        });

        AppCompatButton farm3 = findViewById(R.id.farm_3);
        farm3.setOnClickListener(view -> {
            Config.fileName ="pins3";
            Config.saveConfig(this);
        });
        farm3.setOnLongClickListener(view -> {
            System.out.println("Farm one was long pressed");
            showAlert("pins3");
            return true;
        });

        AppCompatButton farm4 = findViewById(R.id.farm_4);
        farm4.setOnClickListener(view -> {
            Config.fileName ="pins4";
            Config.saveConfig(this);
        });
        farm4.setOnLongClickListener(view -> {
            System.out.println("Farm one was long pressed");
            showAlert("pins4");
            return true;
        });

        AppCompatButton farm5 = findViewById(R.id.farm_5);
        farm5.setOnClickListener(view -> {
            Config.fileName ="pins5";
            Config.saveConfig(this);
        });
        farm5.setOnLongClickListener(view -> {
            System.out.println("Farm one was long pressed");
            showAlert("pins5");
            return true;
        });

        AppCompatButton farm6 = findViewById(R.id.farm_6);
        farm6.setOnClickListener(view -> {
            Config.fileName ="pins6";
            Config.saveConfig(this);
        });
        farm6.setOnLongClickListener(view -> {
            System.out.println("Farm one was long pressed");
            showAlert("pins6");
            return true;
        });

    }
    public void showAlert(String fileName){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert!");
        alert.setMessage("Are you sure you want to delete this farm?");
        alert.setPositiveButton("Yes", (dialogInterface, i) -> {
            Config.fileManager.deleteFile(SettingsActivity.this,fileName);
            dialogInterface.dismiss();
        });
        alert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
        alert.show();
    }

}