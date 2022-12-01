package com.example.workingapp.Activity.marker.googleMap.location;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workingapp.Activity.marker.googleMap.location.data.Stream;
import com.example.workingapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    TextView tv_list_title_view, tv_list_location_view, tv_title_database, tv_location_database, tv_hardness, tv_latitude;
    CardView cv_best_menu;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Stream> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private Button btn_stream_google;

    private View view;

    private Stream stream;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        mRecyclerView = findViewById(R.id.recyclerView);

        tv_title_database = findViewById(R.id.tv_title_database);
        tv_location_database = findViewById(R.id.tv_location_database);
        tv_hardness = findViewById(R.id.tv_hardness);
        tv_latitude = findViewById(R.id.tv_latitude);

        cv_best_menu = findViewById(R.id.cv_best_menu);

        btn_stream_google = findViewById(R.id.btn_stream_google);

        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        btn_stream_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stream = new Stream(tv_list_title_view.getText().toString(), tv_list_location_view.getText().toString());

                databaseReference.child("stream").child("child").setValue(stream);

                tv_title_database.setText(stream.getAPIName());
                tv_location_database.setText(stream.getAPILocation());

                SharedPreferences sharedPreferences = getSharedPreferences("stream.db", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("streamTitle", tv_title_database.getText().toString());
                editor.putString("streamLocation", tv_location_database.getText().toString());
                editor.apply();

                Toast.makeText(getApplicationContext(), "찜했어!", Toast.LENGTH_SHORT).show();
            }
        });

        tv_list_title_view = findViewById(R.id.tv_list_title_view);
        tv_list_location_view = findViewById(R.id.tv_List_location_view);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String location = intent.getStringExtra("snippet");
        String latitude = intent.getStringExtra("latitude");
        String hardness = intent.getStringExtra("hardness");

        tv_list_title_view.setText(title);
        tv_list_location_view.setText(location);
        tv_latitude.setText(latitude);
        tv_hardness.setText(hardness);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("stream.db", MODE_PRIVATE);
        String streamTitle = sharedPreferences.getString("streamTitle", "");
        String streamLocation = sharedPreferences.getString("streamLocation", "");

        tv_title_database.setText(streamTitle);
        tv_location_database.setText(streamLocation);
    }
}