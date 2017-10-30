package com.mad.mellon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


/**
 * This is the main activity for employee users. This page contains welcome message
 * Created by Darren on 21/05/2017.
 */

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        /**
         * Binds all variables to front end elements
         * Getting intents from previous page to be used as information for the details
         */
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:

                        break;
                    case R.id.schedule:
                        Intent intent1 = new Intent(MainActivity.this, Schedule.class);
                        startActivity(intent1);
                        break;
                    case R.id.roster:
                        Intent intent2 = new Intent(MainActivity.this, EmployeeRoster.class);
                        startActivity(intent2);
                        break;
                    case R.id.account:
                        Intent intent3 = new Intent(MainActivity.this, Account.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
}
