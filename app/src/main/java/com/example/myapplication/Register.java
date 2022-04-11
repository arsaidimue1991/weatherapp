package com.example.myapplication;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button registerButton;
    private TextView loginText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
    }

    public void Register() {
        String user = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(user.isEmpty()){
            email.setError("Email can not be empty!");
        }
        if(pass.isEmpty()){
            password.setError("Password can not be empty!");
        }else{
            mAuth.createUserWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                    }else{
                        Toast.makeText(Register.this,"Registration Unsuccessful!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}