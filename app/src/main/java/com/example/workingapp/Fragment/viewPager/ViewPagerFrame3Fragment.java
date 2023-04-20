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

import com.bumptech.glide.Glide;
import com.example.workingapp.Activity.register.RegisterActivity;
import com.example.workingapp.R;
import com.felipecsl.gifimageview.library.GifImageView;

public class ViewPagerFrame3Fragment extends Fragment {

    CardView cvFragmentFestClick;

    GifImageView gifRegisterImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.viewpager_frame_3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cvFragmentFestClick = view.findViewById(R.id.cv_fragment_fest_click);
        gifRegisterImageView = view.findViewById(R.id.gifRegisterImageView);

        Glide.with(requireActivity()).load(R.drawable.viewpager_login_icon).into(gifRegisterImageView);

        cvFragmentFestClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(requireActivity(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}