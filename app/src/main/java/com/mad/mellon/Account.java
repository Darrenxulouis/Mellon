package com.mad.mellon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Account is the activity for account of the employee users
 * This activity allows users to logout.
 * Created by Darren on 21/05/2017.
 */

public class Account extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private final static String WELCOME_TEXT = "Welcome!";
    private final static String EMAIL_INFO_TEXT = "Your email is ";

    public Account() {
    }

    /**
     * OnCreate binds the layout and setting onclick for the screen
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        /*
          Initialize firebase Authentication
         */
        mAuth = FirebaseAuth.getInstance();

        /*
          Binding variables with front end
         */
        TextView nameView = (TextView) findViewById(R.id.fullName);
        TextView emailView = (TextView) findViewById(R.id.email);
        Button logout = (Button) findViewById(R.id.logoutButton);
        String emailText = EMAIL_INFO_TEXT + mAuth.getCurrentUser().getEmail();

        nameView.setText(WELCOME_TEXT);
        emailView.setText(emailText);
        /*
          Logout when the logout button is called.
          Function signout which are provided by firebase is called
         */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


        /*
          Styling bottom navigation bar
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        /*
          Bottom navigation view
          Redirecting to pages of the appropriate button that is clicked
         */
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(Account.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.schedule:
                        Intent intent2 = new Intent(Account.this, Schedule.class);
                        startActivity(intent2);
                        break;
                    case R.id.roster:
                        Intent intent3 = new Intent(Account.this, EmployeeRoster.class);
                        startActivity(intent3);
                        break;
                    case R.id.account:
                        break;
                }
                return false;
            }
        });
    }
}
