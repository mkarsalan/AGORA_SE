package com.patang.agora;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import com.google.firebase.auth.FirebaseAuth;

public class signin extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail;
    EditText editTextPassword;
    ProgressBar progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.login).setOnClickListener(this);
    }

    private void login()
    {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

       /* if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }*/

        //checking if email area is empty or not
        if (email.isEmpty())
        {
            editTextEmail.setError("Please enter Email Address.");
            editTextEmail.requestFocus();
            return;
        }

        //Checking if the email address is valid or not
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please enter a valid Email Address.");
            editTextEmail.requestFocus();
            return;
        }

        //checking if password area is empty or not
        if (password.isEmpty())
        {
            editTextPassword.setError("Please enter your Password.");
            editTextPassword.requestFocus();
            return;
        }


        //NOW REGISTERING THE USER
        progressbar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                //User successfully logged in
                if(task.isSuccessful())
                {
                    /*//change it according to the next screen which will be displayed
                    Intent intent = new Intent (Login.this, newActivity.class);

                    //wont go back to the main screen if press back button
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                    Toast.makeText(getApplicationContext(), "User has been successfully logged in.", Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.login){
           login();
        }
    }
}
