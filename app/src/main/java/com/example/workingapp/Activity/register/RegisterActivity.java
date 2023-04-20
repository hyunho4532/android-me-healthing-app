package com.example.workingapp.Activity.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workingapp.Activity.MainActivity;
import com.example.workingapp.Activity.login.LoginActivity;
import com.example.workingapp.Fragment.ProfileFragment;
import com.example.workingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText etCheckEmail, etCheckPassword;

    private Button btnRegister;
    private TextView tvLoginMove;

    private ImageView ivHiddenShow, ivHiddenNoShow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        etCheckEmail = findViewById(R.id.et_check_email);
        etCheckPassword = findViewById(R.id.et_check_password);
        btnRegister = findViewById(R.id.btnLogin);
        tvLoginMove = findViewById(R.id.tv_loginMove);

        ivHiddenShow = findViewById(R.id.iv_hidden_show);
        ivHiddenNoShow = findViewById(R.id.iv_hidden_no_show);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(etCheckEmail.getText().toString(), etCheckPassword.getText().toString()).addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                            String email = etCheckEmail.getText().toString();
                            String password = etCheckPassword.getText().toString();

                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            mainIntent.putExtra("email", email);
                            mainIntent.putExtra("password", password);
                            startActivity(mainIntent);

                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        tvLoginMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginMoveIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginMoveIntent);
            }
        });

        ivHiddenShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCheckPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivHiddenNoShow.setVisibility(View.VISIBLE);
                ivHiddenShow.setVisibility(View.INVISIBLE);
            }
        });

        ivHiddenNoShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCheckPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivHiddenShow.setVisibility(View.VISIBLE);
                ivHiddenNoShow.setVisibility(View.INVISIBLE);
            }
        });
    }
}