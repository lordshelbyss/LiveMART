package com.example.livemart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    private EditText username, passwordTextView,retypePassword,email,entity;
    private Button Btn;
    private FirebaseAuth mAuth;
    String user, password,retype,Email,Entity;
    private ImageButton imgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Register new user

        mAuth = FirebaseAuth.getInstance();
        // initialising all views through id defined above
        username = findViewById(R.id.username);
        passwordTextView = findViewById(R.id.Password);
        retypePassword=findViewById(R.id.retype_password);
        email=findViewById(R.id.email);
        entity=findViewById(R.id.entity);
        Btn = findViewById(R.id.signUp);
        imgBtn=findViewById(R.id.backBtn);

        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }

    private void registerNewUser()
    {


        // Take the value of two edit texts in Strings

        user = username.getText().toString().trim();
        password = passwordTextView.getText().toString().trim();
        retype=retypePassword.getText().toString().trim();
        Email=email.getText().toString().trim();
        Entity=entity.getText().toString().trim();

        // Validations for input username and password
        if (user.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Username!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (Email.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length()<6) {
            Toast.makeText(getApplicationContext(),
                    "Password should be greater than 5 characters!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (!password.equals(retype)) {
            Toast.makeText(getApplicationContext(),
                    "Please retype password again,Check if CAPS LOCK is turned on!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(Email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        System.out.println(Email);
                        saverFirebaseData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.v("error",e.getMessage());
                    }
                });
    }

    private void saverFirebaseData()
    {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("uid",""+mAuth.getUid());
        hashMap.put("email",""+email);
        hashMap.put("username",""+user);
        hashMap.put("password",""+password);
        hashMap.put("accountType",""+Entity);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}