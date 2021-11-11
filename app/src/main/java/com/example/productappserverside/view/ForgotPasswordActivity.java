package com.example.productappserverside.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.productappserverside.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText editPhoneNumber,editPass;
    Button btnSign;
    FirebaseDatabase database;
    DatabaseReference databaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editPhoneNumber=findViewById(R.id.editEmailUp);
        editPass=findViewById(R.id.editPass);
        btnSign=findViewById(R.id.btnSignIn);

        database=FirebaseDatabase.getInstance();
        databaseUser=database.getReference("User");
    }
}