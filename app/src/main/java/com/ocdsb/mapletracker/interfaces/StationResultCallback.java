package com.ocdsb.mapletracker.interfaces;

import com.ocdsb.mapletracker.data.StationResult;

public interface StationResultCallback {
    // Used to receive a StationResult after an unknown amount of time.
    void onResult(StationResult stationResult);
}
