package com.example.workingapp.Activity.marker.googleMap;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public interface MarkerSetting extends OnMapReadyCallback {

    @Override
    void onMapReady(@NonNull GoogleMap googleMap);
}

