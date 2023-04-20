package com.example.workingapp.Activity.marker.googleMap.place;

import static com.example.workingapp.R.drawable.activity_marker_location;
import static com.example.workingapp.R.drawable.activity_parri_baguette;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import noman.googleplaces.Place;

public class MarkerPlaceSetting extends AppCompatActivity {

    public void BreadSettings(Place place, MarkerOptions markerPlaceOptions, LatLng latLng, String markerSnippet) {
        if (place.getName().contains("파리")) {
            @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapDrawable1 = (BitmapDrawable)getResources().getDrawable(activity_parri_baguette);

            Bitmap pariBitmap =bitmapDrawable1.getBitmap();

            Bitmap markerPariBitmap = Bitmap.createScaledBitmap(pariBitmap, 120, 120, false);

            markerPlaceOptions = new MarkerOptions();
            markerPlaceOptions.position(latLng);
            markerPlaceOptions.title(place.getName());
            markerPlaceOptions.snippet(markerSnippet);
            markerPlaceOptions.icon(BitmapDescriptorFactory.fromBitmap(markerPariBitmap));
        }
    }
}
