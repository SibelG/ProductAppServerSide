package com.example.productappserverside.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.productappserverside.R;
import com.example.productappserverside.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText editName,editEmailNumber,editPass,repeatPass,userName;
    Button btnSignUp;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        editEmailNumber = findViewById(R.id.editEmailUp);
        userName= findViewById(R.id.userNameUp);
        editPass = findViewById(R.id.editPass);
        repeatPass = findViewById(R.id.forgotPass);
        btnSignUp = findViewById(R.id.btnSignUp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUser = database.getReference("User");
        firebaseAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email = editEmailNumber.getText().toString();
                String password = editPass.getText().toString();
                String againPass = repeatPass.getText().toString();
                String name=userName.getText().toString();
                if (!email.isEmpty() && !password.isEmpty() && !againPass.isEmpty()) {
                    if (password.equals(againPass)) {

                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                String userId = databaseUser.push().getKey();
                                User user=new User(name,password,email,"false");
                                databaseUser.child(userId).setValue(user);

                                Toast.makeText(SignUpActivity.this, "User Created", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(SignUpActivity.this, "Your Entered the Passwords don't match!!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    CheckUnit();
                }
            }
        });
    }
    private void CheckUnit(){
        if(TextUtils.isEmpty(editEmailNumber.getText().toString())){
            editEmailNumber.setText("Enter your Email");
        }if(TextUtils.isEmpty(editPass.getText().toString())){
            editPass.setText("Enter your password");
        }if(TextUtils.isEmpty(repeatPass.getText().toString()))
            repeatPass.setText("Confirm your password");
    }
}

