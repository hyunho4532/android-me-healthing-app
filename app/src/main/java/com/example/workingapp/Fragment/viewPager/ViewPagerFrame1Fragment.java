package com.example.workingapp.Fragment.viewPager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.workingapp.Activity.marker.ActivityMarkerActivity;
import com.example.workingapp.R;

public class ViewPagerFrame1Fragment extends Fragment {

    private CardView cvFragmentFestClick;

    private ImageView gifMarkerFestImageView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvFragmentFestClick = view.findViewById(R.id.cv_fragment_fest_click);
        gifMarkerFestImageView = view.findViewById(R.id.gifMarkerFestImageView);

        Glide.with(requireActivity()).load(R.drawable.viewpager_run_icon).into(gifMarkerFestImageView);

        cvFragmentFestClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent markerIntent = new Intent(requireActivity(), ActivityMarkerActivity.class);
                startActivity(markerIntent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.viewpager_frame_1, container, false);
    }
}