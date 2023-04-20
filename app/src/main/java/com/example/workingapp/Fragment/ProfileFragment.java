package com.example.workingapp.Fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.workingapp.Activity.MainActivity;
import com.example.workingapp.Activity.register.RegisterActivity;
import com.example.workingapp.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.units.qual.A;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseStorage storage;

    CardView mCvRegisterMenu;
    Button mBtnRegister;

    private TextView mTvEmailTitle;

    private static final int REQUEST_CODE = 0;

    ConstraintLayout constraint_layout;

    ImageView mLogoutClick, mGalleryView;

    CircleImageView mProfileUser;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        mCvRegisterMenu = view.findViewById(R.id.cv_register_menu);
        mBtnRegister = view.findViewById(R.id.btnMoveRegister);

        mLogoutClick = view.findViewById(R.id.iv_logout_click);

        constraint_layout = view.findViewById(R.id.constraint_layout);

        mProfileUser = view.findViewById(R.id.iv_profile_user);
        mGalleryView = view.findViewById(R.id.iv_gallery_view);

        mTvEmailTitle = view.findViewById(R.id.tv_email_title);

        initImageViewProfile();

        constraint_layout.setAlpha(0.3f);
        mLogoutClick.setClickable(false);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerMoveIntent = new Intent(requireActivity(), RegisterActivity.class);
                startActivity(registerMoveIntent);
            }
        });

        mLogoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("로그아웃 또는 계정 탈퇴");

                builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        dialogInterface.dismiss();

                        Intent mainIntent = new Intent(requireActivity(), MainActivity.class);
                        startActivity(mainIntent);
                    }
                });

                builder.setNegativeButton("계정 탈퇴", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder noticeDeleteDialog = new AlertDialog.Builder(requireActivity());

                        noticeDeleteDialog.setTitle("정말로 탈퇴하시겠습니까?? 탈퇴할 시 계정이 완전 삭제됩니다.");

                        noticeDeleteDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(requireActivity(), "계정 탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                            Intent mainIntent = new Intent(requireActivity(), MainActivity.class);
                                            startActivity(mainIntent);

                                            dialogInterface.dismiss();
                                        }
                                    }
                                });
                            }
                        });

                        noticeDeleteDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        noticeDeleteDialog.show();

                        storage.getReference().child("photo").child("1.png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        });
                    }
                });

                builder.show();
            }
        });

        StorageReference submitProfile = storageReference.child("photo/1.png");
        submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(requireActivity()).load(uri).into(mProfileUser);
            }
        });
    }

    private void initImageViewProfile() {
        mGalleryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryMoveIntent = new Intent();
                galleryMoveIntent.setType("image/*");
                galleryMoveIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(galleryMoveIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {

                    assert data != null;
                    Uri uri = data.getData();

                    Glide.with(requireActivity())
                            .load(uri)
                            .into(mProfileUser);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mAuth.getUid() != null) {
            mLogoutClick.setClickable(true);
            mCvRegisterMenu.setVisibility(View.INVISIBLE);
            constraint_layout.setAlpha(1f);

            String name = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

            mTvEmailTitle.setText(name);

        } else {
            mLogoutClick.setClickable(false);
            mCvRegisterMenu.setVisibility(View.VISIBLE);
            constraint_layout.setAlpha(0.3f);
        }
    }
}