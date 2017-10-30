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
 * AdminAccount class is an activity for account page for administrator account
 * This page allow administrators to logout
 * Created by Darren on 1/06/2017.
 */

public class AdminAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String WELCOME_ADMIN_TEXT = "Welcome, Admin!";
    private static final String EMAIL_TEXT = "Your are logged in using ";

    /**
     * OnCreate sets the layout and the button of front end to the back end code
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_account);

        /*
          getting authentication information from firebase and assigning to mAuth variable
         */
        mAuth = FirebaseAuth.getInstance();

        /*
          Binding textviews and buttons variable to their front end counterpart
         */
        TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        TextView emailView = (TextView) findViewById(R.id.email);
        Button logout = (Button) findViewById(R.id.logoutButton);

        /*
          Setting welcome text of the admin account
         */
        String emailText = EMAIL_TEXT + mAuth.getCurrentUser().getEmail();
        welcomeText.setText(WELCOME_ADMIN_TEXT);
        emailView.setText(emailText);

        /*
          Logout when the logout button is clicked
          Firebase signOut method is called
         */
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        /*
          Navigation bar styling
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
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
                        Intent intent1 = new Intent(AdminAccount.this, AdminMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.staff:
                        Intent intent2 = new Intent(AdminAccount.this, AdminStaff.class);
                        startActivity(intent2);
                        break;
                    case R.id.roster:
                        Intent intent3 = new Intent(AdminAccount.this, AdminRoster.class);
                        startActivity(intent3);
                        break;
                    case R.id.premise:
                        Intent intent4 = new Intent(AdminAccount.this, AdminPremise.class);
                        startActivity(intent4);
                        break;
                    case R.id.account:
                        break;
                }
                return false;
            }
        });
    }
}
