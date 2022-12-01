package com.example.workingapp.Activity.marker.googleMap.location.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MarkerClusterItem implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;

    public MarkerClusterItem(double lat, double lng, String title) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Nullable
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }
}
