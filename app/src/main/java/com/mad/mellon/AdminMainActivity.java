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
 * The home page of the admin users. This page has a welcome message
 * Created by Darren on 1/06/2017.
 */

public class AdminMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main);

        /*
          Navigation bar styling
         */
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
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

                        break;
                    case R.id.staff:
                        Intent intent1 = new Intent(AdminMainActivity.this, AdminStaff.class);
                        startActivity(intent1);
                        break;
                    case R.id.roster:
                        Intent intent2 = new Intent(AdminMainActivity.this, AdminRoster.class);
                        startActivity(intent2);
                        break;
                    case R.id.premise:
                        Intent intent3 = new Intent(AdminMainActivity.this, AdminPremise.class);
                        startActivity(intent3);
                        break;
                    case R.id.account:
                        Intent intent4 = new Intent(AdminMainActivity.this, AdminAccount.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
}
