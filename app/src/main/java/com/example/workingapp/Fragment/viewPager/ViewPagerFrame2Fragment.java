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
import com.example.workingapp.Activity.board.introduce.IntroduceBoardActivity;
import com.example.workingapp.R;

public class ViewPagerFrame2Fragment extends Fragment {

    private CardView cvFragmentFestClick;
    private ImageView gifIntroduceBoardImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.viewpager_frame_2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gifIntroduceBoardImageView = view.findViewById(R.id.gifIntroduceBoardImageView);

        cvFragmentFestClick = view.findViewById(R.id.cv_fragment_fest_click);

        Glide.with(requireActivity()).load(R.drawable.viewpager_board_introduce_icon).into(gifIntroduceBoardImageView);

        cvFragmentFestClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent introduceMoveIntent = new Intent(requireActivity(), IntroduceBoardActivity.class);
                startActivity(introduceMoveIntent);
            }
        });
    }
}