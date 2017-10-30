package com.mad.mellon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This activity allow users to login and checking credentials and redirecting them into
 * activity based on the type of user
 * Created by Darren on 31/05/2017.
 */

public class Login extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private FirebaseAuth mAuth;
    private static final String ADMIN_LOGIN = "admin@admin.com";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        mEmail = (EditText) findViewById(R.id.emailField);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        linearLayout = (LinearLayout) findViewById(R.id.labelLayout);
        mPassword = (EditText) findViewById(R.id.passwordField);
        Button mButtonLogin = (Button) findViewById(R.id.loginButton);
        Button mButtonRegister = (Button) findViewById(R.id.registerButton);

        /*
        redirecting users to register page
         */
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    /*
    Start the login process. This code checks for user input and returning error messages
    if there are any error in the login process
     */
    public void login()
    {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        /*
        Once the error checking is done, this block of code allows users to login using
        firebase authentication
         */
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    if(mAuth.getCurrentUser().getEmail().toLowerCase().equals(ADMIN_LOGIN))
                    {
                        startActivity(new Intent(Login.this , AdminMainActivity.class));
                    }
                    else
                    {
                        startActivity(new Intent(Login.this , MainActivity.class));
                    }

                }
                else
                {

                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Login failed. Please check your login details", Toast.LENGTH_LONG).show();
                }
                }
            });
        }
}
