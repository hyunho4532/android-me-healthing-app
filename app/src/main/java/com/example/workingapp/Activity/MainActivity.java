package com.example.workingapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.workingapp.Fragment.FoodFragment;
import com.example.workingapp.Fragment.BoardFragment;
import com.example.workingapp.Fragment.ProfileFragment;
import com.example.workingapp.Fragment.WorkFragment;
import com.example.workingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FoodFragment foodFragment = new FoodFragment();
        WorkFragment workFragment = new WorkFragment();
        BoardFragment mainFragment = new BoardFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, workFragment).commitAllowingStateLoss();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.fragment_a:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, workFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.fragment_b:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, foodFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.fragment_c:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, mainFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.fragment_d:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment).commitAllowingStateLoss();

                    default:
                        return true;
                }
            }
        });
    }
}