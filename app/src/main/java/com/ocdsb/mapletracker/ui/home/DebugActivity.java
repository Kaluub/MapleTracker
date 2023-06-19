package com.ocdsb.mapletracker.ui.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ocdsb.mapletracker.R;

import java.util.Objects;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}