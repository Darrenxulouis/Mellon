package com.mad.mellon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This is a class that holds schedule fragments for employee users
 * Created by Darren on 21/05/2017.
 */

public class Schedule extends AppCompatActivity {

    private SectionsPagerAdapter mSectionPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("Request");
        tabLayout.getTabAt(1).setText("Requested");


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
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
                        Intent intent1 = new Intent(Schedule.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.schedule:

                        break;
                    case R.id.roster:
                        Intent intent2 = new Intent(Schedule.this, EmployeeRoster.class);
                        startActivity(intent2);
                        break;
                    case R.id.account:
                        Intent intent3 = new Intent(Schedule.this, Account.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }

    /*
    Adding fragments to the page
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ScheduleRequestDayOffFragment());
        adapter.addFragment(new ScheduleRequestedFragment());
        viewPager.setAdapter(adapter);
    }
}
