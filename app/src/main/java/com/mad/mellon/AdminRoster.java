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
 * This class host the fragments of rosters for admin users
 * Created by Darren on 1/06/2017.
 */

public class AdminRoster extends AppCompatActivity {
    private SectionsPagerAdapter mSectionPagerAdapter;

    /**
     * Section pager adapter allows pages to be shown as tabs
     * This allows the use of fragments
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_roster);

        mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("Create Roster");
        tabLayout.getTabAt(1).setText("View Roster");


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
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
                        Intent intent1 = new Intent(AdminRoster.this, AdminMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.staff:
                        Intent intent2 = new Intent(AdminRoster.this, AdminStaff.class);
                        startActivity(intent2);
                        break;
                    case R.id.roster:
                        break;
                    case R.id.premise:
                        Intent intent3 = new Intent(AdminRoster.this, AdminPremise.class);
                        startActivity(intent3);
                        break;
                    case R.id.account:
                        Intent intent4 = new Intent(AdminRoster.this, AdminAccount.class);
                        startActivity(intent4);
                        break;
                }



                return false;
            }
        });
    }

    /*
    This function sets up the fragment by adding them into array that will be shown later using adapter
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AdminCreateRosterFragment());
        adapter.addFragment(new AdminViewRosterFragment());
        viewPager.setAdapter(adapter);
    }
}
