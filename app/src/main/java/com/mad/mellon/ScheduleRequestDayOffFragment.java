package com.mad.mellon;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * This fragments allow employee users to request for day off
 * Created by Darren on 28/05/2017.
 */

public class ScheduleRequestDayOffFragment extends Fragment {
    private DatabaseReference mDatabase;
    private DatePicker datePicker;
    private String email;
    private static final String DAYS_OFF = "DaysOff";


    /*
    Binding all elements and allowing users to input using datapicker
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.schedule_request_day_off_fragment, container,false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(DAYS_OFF);
        Button requestButton = (Button) view.findViewById(R.id.requestButton);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail().toString();


        /*
        submit button that will add day off to the database
         */
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                Date date = calendar.getTime();


                DaysOff daysOff = new DaysOff();
                daysOff.setEmployeeEmail(email);
                daysOff.setDate(date);

                /*
                Adding day off to the database as DaysOff object
                 */
                mDatabase.push().setValue(daysOff).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Stored..", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Failed.. Please Try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return view;
    }
}
