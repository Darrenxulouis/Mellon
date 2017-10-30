package com.mad.mellon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class allows employee users to view details of the roster
 * Created by Darren on 3/06/2017.
 */

public class EmployeeRosterDetails extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TimeslotAdapter timeslotAdapter;
    private ArrayList<Timeslot> timeslots= new ArrayList<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy - MMM - dd");
    private static final String EMPLOYEE_ROSTER = "EmployeeRoster";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String PREMISE = "premise";

    /**
     * Binds all variables to front end elements
     * Getting intents from previous page to be used as information for the details
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roster_details);

        Button delete = (Button) findViewById(R.id.deleteButton);

        delete.setVisibility(View.GONE);

        TextView dateView = (TextView) findViewById(R.id.rosterDate);
        TextView premiseView = (TextView) findViewById(R.id.rosterPremise);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(EMPLOYEE_ROSTER);

        /*
        Getting intent from the previous activity and storing into calendar so it can be shown
         */
        Intent intent = getIntent();
        final String premise = intent.getStringExtra(PREMISE);
        int year = intent.getIntExtra(YEAR, 0);
        int month = intent.getIntExtra(MONTH, 0);
        int day = intent.getIntExtra(DAY, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        Date date = calendar.getTime();
        final String rosterDate = DATE_FORMAT.format(date);

        dateView.setText(rosterDate);
        premiseView.setText(premise);


        /*
        This code gets roster information. If the information matches the information passed from intent, it will show
        the details using adapter
         */
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Roster roster = dataSnapshot.getValue(Roster.class);
                String rosterDateString = DATE_FORMAT.format(roster.getDate());

                if(premise.equals(roster.getPremise()))
                {
                    if(rosterDateString.equals(rosterDate))
                    {
                        timeslots = roster.getRoster();
                        recyclerView = (RecyclerView) findViewById(R.id.rosterDetailsRecyclerView);
                        timeslotAdapter = new TimeslotAdapter(timeslots);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(timeslotAdapter);
                        timeslotAdapter.notifyDataSetChanged();
                    }
                }

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
    }
}
