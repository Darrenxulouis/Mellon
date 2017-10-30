package com.mad.mellon;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This page allow users to register
 */
public class Register extends AppCompatActivity {
    private EditText mFullName;
    private EditText mEmail;
    private EditText mPassword;
    private Spinner mAccountType;
    private FirebaseAuth mAuth;
    private String fullName, email, password;
    private static final String EMPLOYER = "Employer";
    Employee employee = new Employee();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAccountType = (Spinner) findViewById(R.id.spinner);
        mFullName = (EditText) findViewById(R.id.fullNameInput);
        mEmail = (EditText) findViewById(R.id.emailInput);
        mPassword = (EditText) findViewById(R.id.passwordInput);
        Button mButton = (Button) findViewById(R.id.submitButton);
        mAuth = FirebaseAuth.getInstance();

        /*
        getting the details and calling register function to register user
         */
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = mFullName.getText().toString();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                register();

                }
        });
    }

    /*
    Register function checks for user input and errors it might generate.
    It returns error messages if there is an error in input
     */
    public void register(){
        if(TextUtils.isEmpty(fullName))
        {
            Toast.makeText(getApplicationContext(), "Please enter Name", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(), "Please enter Email", Toast.LENGTH_LONG).show();
            return;
        }
        if(mAccountType.getSelectedItem().toString().equalsIgnoreCase(EMPLOYER))
        {
            Toast.makeText(getApplicationContext(), "Employee registration is not yet available..", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(getApplicationContext(), "Please enter Password", Toast.LENGTH_LONG).show();
            return;
        }
        else if(password.length() < 8)
        {
            Toast.makeText(getApplicationContext(), "Password has to be minimum 8 character", Toast.LENGTH_LONG).show();
            return;
        }


        /*
        Adding users to database once error checking is done
         */
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Register Successful. Redirecting..", Toast.LENGTH_LONG).show();
                    addEmployeeData();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Register Failed. Invalid Email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*
    Adding employee data once users are registered to database
     */
    public void addEmployeeData()
    {
        String mType = mAccountType.getSelectedItem().toString();
        employee.setFullName(fullName);
        employee.setEmail(email);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(mType);
        mDatabase.push().setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}
