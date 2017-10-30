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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * This is a fragment class of roster for admin that allow admin users to see overview of rosters that have been made
 * in rows
 * Created by Darren on 1/06/2017.
 */

public class AdminViewRosterFragment extends Fragment {
    private RosterAdapter rosterAdapter;
    private ArrayList<Roster> rosters = new ArrayList<>();
    private static final String EMPLOYEE_ROSTER = "EmployeeRoster";

    /**
     * Binds all the view variable to the front end.
     * Showing the list of rosters using recycler view and adapter
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.admin_view_roster_fragment, container,false);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(EMPLOYEE_ROSTER);

        /*
        Setting up the recycler view and setting the adapter
         */
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.AdminRosterRecyclerView);
        rosterAdapter = new RosterAdapter(getContext(), rosters);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(rosterAdapter);

        /*
        adding roster to the array so it can be shown
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
        return view;
    }
}
