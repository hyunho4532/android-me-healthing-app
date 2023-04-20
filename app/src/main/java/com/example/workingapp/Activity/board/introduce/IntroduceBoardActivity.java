package com.example.workingapp.Activity.board.introduce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workingapp.Activity.board.introduce.adapter.UserAdapter;
import com.example.workingapp.Activity.board.introduce.data.UserDTO;
import com.example.workingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class IntroduceBoardActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etHobby;

    private CircleImageView ivGalleryClick;

    private ArrayList<UserDTO> userDTOArrayList;

    private static final int REQUEST_CODE = 0;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_board);

        etName = findViewById(R.id.et_name);
        etHobby = findViewById(R.id.et_hobby);

        ivGalleryClick = findViewById(R.id.iv_gallery_click);

        userDTOArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        RecyclerView recyclerView = findViewById(R.id.introduce_recycler);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("message").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                UserDTO userDTO = snapshot.getValue(UserDTO.class);
                assert userDTO != null;

                userDTOArrayList.add(userDTO);

                UserAdapter userAdapter = new UserAdapter(userDTOArrayList, IntroduceBoardActivity.this);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ivGalleryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent, REQUEST_CODE);
            }
        });
    }

    public void clickButton(View view) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        UserDTO userDTO = new UserDTO(etName.getText().toString(), etHobby.getText().toString());
        database.child("message").push().setValue(userDTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri dataUri = data.getData();

                Glide.with(IntroduceBoardActivity.this)
                        .load(dataUri)
                        .into(ivGalleryClick);
            }
        }
    }
}