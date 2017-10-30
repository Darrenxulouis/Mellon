package com.mad.mellon;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * This page allows employee users to view requested days off
 * This page is a fragment of schedule
 * Created by Darren on 28/05/2017.
 */

public class ScheduleRequestedFragment extends Fragment {
    RecyclerView recyclerView;
    DaysOffAdapter daysOffAdapter;
    FirebaseAuth mAuth;
    String email;
    ArrayList<DaysOff> daysOffs = new ArrayList<>();
    ArrayList<String> daysOffsKeys = new ArrayList<>();
    private static final String DAYS_OFF = "DaysOff";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.schedule_requested_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail().toString();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(DAYS_OFF);

        /*
        Setting up the adapter
         */
        recyclerView = (RecyclerView) view.findViewById(R.id.DaysOffRecyclerView);
        daysOffAdapter = new DaysOffAdapter(getContext(),daysOffs, daysOffsKeys);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(daysOffAdapter);


        /*
        This block gets data from database and adding into the array so it can be shown by adapter
         */
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DaysOff dbResult = dataSnapshot.getValue(DaysOff.class);

                String resultEmail = dbResult.getEmployeeEmail();

                Calendar cal = Calendar.getInstance();
                cal.setTime(dbResult.getDate());
                Date date = cal.getTime();

                DaysOff daysOff = new DaysOff();
                daysOff.setDate(date);
                daysOff.setEmployeeEmail(resultEmail);


                /*
                Showing only the days off requested by the employee logged in
                 */
                if(dbResult.getEmployeeEmail().equalsIgnoreCase(email))
                {
                    daysOffsKeys.add(dataSnapshot.getKey());
                    daysOffs.add(daysOff);
                }


                daysOffAdapter.notifyDataSetChanged();
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

        return view;
    }


}
