package com.example.workingapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workingapp.Activity.marker.ActivityMarkerActivity;
import com.example.workingapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class FoodFragment extends Fragment  {

    CardView cv_food_health_list, cv_food_insert;

    EditText et_food_title, et_kcal_title;

    ProgressBar progressBar;

    Button btn_food_insert;

    TextView tv_count_step, tv_goal_step;

    int resultProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cv_food_health_list = view.findViewById(R.id.cv_food_health_list);
        cv_food_insert = view.findViewById(R.id.cv_food_insert);

        et_food_title = view.findViewById(R.id.et_food_title);
        et_kcal_title = view.findViewById(R.id.et_kcal_title);

        tv_count_step = view.findViewById(R.id.tv_count_step);
        tv_goal_step = view.findViewById(R.id.tv_goal_step);

        progressBar = view.findViewById(R.id.progressbar);

        btn_food_insert = view.findViewById(R.id.btn_food_insert);

        cv_food_health_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivityMarkerActivity.class);
                startActivity(intent);
            }
        });

        btn_food_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    int goalKcal = Integer.parseInt(et_food_title.getText().toString());
                    int resultKcal = Integer.parseInt(et_kcal_title.getText().toString());

                    resultProgress += resultKcal;

                    tv_goal_step.setText(String.valueOf(goalKcal));
                    tv_count_step.setText(String.valueOf(resultProgress));

                    progressBar.setMax(goalKcal);
                    progressBar.setProgress(resultProgress);

                    if (Integer.parseInt(tv_count_step.getText().toString()) > Integer.parseInt(tv_goal_step.getText().toString())) {
                        Toast.makeText(requireActivity(), "목표 칼로리 돌파 다시 설정해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("kcalList", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("inputKcal", Integer.parseInt(tv_count_step.getText().toString()));
                    editor.putInt("goalKcal", Integer.parseInt(tv_goal_step.getText().toString()));

                    editor.apply();

                } catch (NumberFormatException numberFormatException) {
                    int resultKcal = Integer.parseInt(et_kcal_title.getText().toString());

                    resultProgress += resultKcal;

                    tv_count_step.setText(String.valueOf(resultProgress));

                    progressBar.setProgress(resultProgress);

                    if (Integer.parseInt(tv_count_step.getText().toString()) > Integer.parseInt(tv_goal_step.getText().toString())) {
                        Toast.makeText(requireActivity(), "목표 칼로리 돌파 다시 설정해주세요.", Toast.LENGTH_SHORT).show();
                    }

                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("kcalList", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("inputKcal", Integer.parseInt(tv_count_step.getText().toString()));
                    editor.putInt("goalKcal", Integer.parseInt(tv_goal_step.getText().toString()));

                    editor.apply();
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("kcalList", Context.MODE_PRIVATE);
        int inputKcal = sharedPreferences.getInt("inputKcal", 0);
        int goalKcal = sharedPreferences.getInt("goalKcal", 0);

        tv_count_step.setText(String.valueOf(inputKcal));
        tv_goal_step.setText(String.valueOf(goalKcal));

        progressBar.setProgress(Integer.parseInt(tv_count_step.getText().toString()));
        progressBar.setMax(Integer.parseInt(tv_goal_step.getText().toString()));
    }
}