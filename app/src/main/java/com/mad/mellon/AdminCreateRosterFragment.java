package com.mad.mellon;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * This class allow admin users to pick a date and premise as a first step to creating a roster
 * Created by Darren on 1/06/2017.
 */

public class AdminCreateRosterFragment extends Fragment {
    private DatePicker datePicker;
    private Spinner premiseInput;
    private ArrayList<String> premise = new ArrayList<>();
    private ArrayList<Roster> rosters = new ArrayList<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MMM-dd");
    private static final String PREMISE = "Premise";
    private static final String EMPLOYEE_ROSTER = "EmployeeRoster";

    /**
     * Create a fragment view of roster and binding all the buttons and edit text to the corresponding
     * activity's front end
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.admin_add_roster_fragment, container,false);
        Button submitButton = (Button) view.findViewById(R.id.createNewRosterButton);
        datePicker = (DatePicker) view.findViewById(R.id.newRosterDatePicker);
        premiseInput = (Spinner) view.findViewById(R.id.premiseInput);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(PREMISE);
        DatabaseReference mRosterDatabase = FirebaseDatabase.getInstance().getReference().child(EMPLOYEE_ROSTER);

        /*
        Setting adapter for premise input
         */
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, premise);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        premiseInput.setAdapter(adapter);

        /*
        This block of code populates the premise ArrayList
        based on the data acquired from the database
         */
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                premise.add(value);
                adapter.notifyDataSetChanged();
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

        /*
        This block of code is populating the array list of saved rostered
        so it can be compared with the new one that will be created.
        If the date and premise is found in the array list, it cannot proceed
        This is to prevent unwanted duplication
         */
        mRosterDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Roster roster = dataSnapshot.getValue(Roster.class);
                rosters.add(roster);
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


        /*
        This submit button check the inputs and comparing to the array list data
         */
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(premise.isEmpty())
                {
                    Toast.makeText(getContext(),
                            "Please add premise in the premise tab before continuing", Toast.LENGTH_LONG).show();
                    return;
                }

                if(premiseInput.getSelectedItem() == null)
                {
                    Toast.makeText(getContext(),
                            "Please pick a premise", Toast.LENGTH_LONG).show();
                    return;
                }
                String premise = premiseInput.getSelectedItem().toString();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                String date = DATE_FORMAT.format(calendar.getTime());
                for(Roster roster : rosters)
                {
                    if(roster.getPremise().equals(premise))
                    {
                        String rosteredDate = DATE_FORMAT.format(roster.getDate());
                        if(rosteredDate.equals(date))
                        {
                            Toast.makeText(getContext(),
                                    "A roster of this date and premise exists", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }

                Intent intent = new Intent(getContext() , AdminCreateIndividualRoster.class);
                intent.putExtra("premise", premise);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });

        return view;
    }
}
