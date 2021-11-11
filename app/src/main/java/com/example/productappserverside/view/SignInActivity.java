package com.example.productappserverside.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.productappserverside.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    EditText editEmailNumber,editPass;
    Button btnSign;
    FirebaseDatabase database;
    DatabaseReference databaseUser;
    FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editEmailNumber=findViewById(R.id.editEmail);
        editPass=findViewById(R.id.editPass);
        btnSign=findViewById(R.id.btnSignIn);

        database=FirebaseDatabase.getInstance();
        databaseUser=database.getReference("User");
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser(); // authenticated user

        if(firebaseUser != null){ // check user session

            Intent i = new Intent(SignInActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }

    }


    public void btnSignIn(View view){
        String email = editEmailNumber.getText().toString();
        String password = editPass.getText().toString();

        if(email.isEmpty() || password.isEmpty()){

            Toast.makeText(getApplicationContext(),"Lütfen gerekli alanları doldurunuz!",Toast.LENGTH_SHORT).show();

        }else{

            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignInActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }
    }



}