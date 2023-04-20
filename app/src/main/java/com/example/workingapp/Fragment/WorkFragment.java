package com.example.workingapp.Fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workingapp.Activity.exercise.SquatActivity;
import com.example.workingapp.Fragment.viewPager.adapter.SettingAdapter;
import com.example.workingapp.R;

public class WorkFragment extends Fragment implements SensorEventListener {

    SensorManager sensorManager;
    TextView tvCountStep, tvGoalStep;

    int currentSteps = 0;
    Sensor stepCountSensor;

    EditText et_goal_count;
    Button btn_goal_save;

    ProgressBar progressBar;

    View targetView;
    boolean drawerToggle;

    TextView tv_exercise_title, tv_click_me, tv_day_goal_title;

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 3;

    private CardView cvSquatMoveExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCountStep = view.findViewById(R.id.tv_count_step);
        tvGoalStep = view.findViewById(R.id.tv_goal_step);

        progressBar = view.findViewById(R.id.progressbar);
        et_goal_count = view.findViewById(R.id.et_goal_count);
        btn_goal_save = view.findViewById(R.id.btn_save_goal);

        tv_exercise_title = view.findViewById(R.id.tv_exercise_title);
        tv_click_me = view.findViewById(R.id.tv_click_me);

        targetView = view.findViewById(R.id.content);

        cvSquatMoveExercise = view.findViewById(R.id.cv_squat_exercise);

        mPager = view.findViewById(R.id.viewpager);
        pagerAdapter = new SettingAdapter(requireActivity(), num_page);

        mPager.setAdapter(pagerAdapter);

        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        mPager.setCurrentItem(1000);
        mPager.setOffscreenPageLimit(4);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }
        });

        class DrawButtonClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                if (!drawerToggle) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(targetView, "translationY", -1200);
                    objectAnimator.setDuration(500);
                    objectAnimator.start();

                    tv_click_me.setVisibility(View.INVISIBLE);
                    tv_exercise_title.setVisibility(View.VISIBLE);

                } else {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(targetView, "translationY", 0);
                    objectAnimator.setDuration(500);
                    objectAnimator.start();

                    tv_click_me.setVisibility(View.VISIBLE);
                    tv_exercise_title.setVisibility(View.INVISIBLE);
                }

                drawerToggle = !drawerToggle;
            }
        }

        btn_goal_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setMax(Integer.parseInt(et_goal_count.getText().toString()));
                Toast.makeText(getActivity(), "목표 걸음수가 입력되었습니다.", Toast.LENGTH_SHORT).show();

                tvGoalStep.setText(et_goal_count.getText().toString());

                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("work", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("totalCount", tvGoalStep.getText().toString());
                editor.apply();
            }
        });

        cvSquatMoveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent squatIntent = new Intent(requireActivity(), SquatActivity.class);
                startActivity(squatIntent);
            }
        });

        targetView.setOnClickListener(new DrawButtonClickListener());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if (stepCountSensor == null) {
            Toast.makeText(getActivity(), "뛰세요", Toast.LENGTH_SHORT).show();
        }

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        sensorManager.registerListener((SensorEventListener) this, stepCountSensor, SensorManager.SENSOR_DELAY_FASTEST);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("work", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("totalCount", "");

        tvGoalStep.setText(result);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (sensorEvent.values[0] == 1.0f) {
                tvCountStep.setText(String.valueOf(currentSteps));
                progressBar.setProgress(Integer.parseInt(tvCountStep.getText().toString()));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}