package com.mad.mellon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * This class allows employee to view roster that has been created
 * Created by Darren on 21/05/2017.
 */

public class EmployeeRoster extends AppCompatActivity {
    private RosterAdapter rosterAdapter;
    private ArrayList<Roster> rosters = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roster);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("EmployeeRoster");

        /*
        Setting up the recycler view and adapter
         */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.EmployeeRosterRecyclerView);
        rosterAdapter = new RosterAdapter(getApplicationContext(), rosters);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rosterAdapter);

        /*
        getting all the saved roster in database and populating into an array list to be used later
         */
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Roster roster = dataSnapshot.getValue(Roster.class);
                rosters.add(roster);
                rosterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                        Intent intent1 = new Intent(EmployeeRoster.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.schedule:
                        Intent intent2 = new Intent(EmployeeRoster.this, Schedule.class);
                        startActivity(intent2);
                        break;
                    case R.id.roster:

                        break;
                    case R.id.account:
                        Intent intent3 = new Intent(EmployeeRoster.this, Account.class);
                        startActivity(intent3);
                        break;
                }

                return false;
            }
        });
    }
}
