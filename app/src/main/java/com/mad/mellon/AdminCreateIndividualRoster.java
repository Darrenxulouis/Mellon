package com.mad.mellon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.mad.mellon.R.color.black;

/**
 * This class allows admin users to create roster by adding timeslots
 * that has a spinner populated with employee names
 * Created by Darren on 1/06/2017.
 */

public class AdminCreateIndividualRoster extends AppCompatActivity{
    private String premise;
    private String date;
    private ArrayList<Integer> rosteredEmployee = new ArrayList<>();
    private DatabaseReference mRosterDatabase;
    private int staffNum;
    private Calendar calendar;
    private Roster roster;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd EEEE");
    private static final String EMPLOYEE = "Employee";
    private static final String DAYS_OFF = "DaysOff";
    private static final String EMPLOYEE_ROSTER = "EmployeeRoster";

    /**
     * Binding the variables to the front end buttons and setting
     * behavior when the button is clicked
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
          Binding buttons to the front end counterpart and initializing database variable
         */
        setContentView(R.layout.admin_create_individual_roster);
        Button addField = (Button) findViewById(R.id.addField);
        Button addRoster = (Button) findViewById(R.id.addRoster);
        Button removeField = (Button) findViewById(R.id.removeField);
        TextView dateLabel = (TextView) findViewById(R.id.dateLabel);
        TextView premiseLabel = (TextView) findViewById(R.id.premiseLabel);
        final LinearLayout content = (LinearLayout) findViewById(R.id.middleContent);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(EMPLOYEE);
        DatabaseReference mDaysOffDatabase = FirebaseDatabase.getInstance().getReference().child(DAYS_OFF);
        mRosterDatabase = FirebaseDatabase.getInstance().getReference().child(EMPLOYEE_ROSTER);

        /*
          Getting intent from the page before
          The page before is AdminCreateRosterFragment which pass int information of day, month, and year
          along with premise which is the main attribute that makes a roster unique
         */
        Intent intent = getIntent();
        premise = intent.getStringExtra("premise");
        int day = intent.getIntExtra("day", 0);
        int month = intent.getIntExtra("month", 0);
        int year = intent.getIntExtra("year", 0);

        /*
          Setting the day, month, and year into calendar to later be formatted as string
          so it can be shown as text
         */
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        date = DATE_FORMAT.format(calendar.getTime());

        /*
          Initializing array list for employees, the names, and daysoff
          which are crucial information to writing a roster
         */
        final ArrayList<Employee> employees = new ArrayList<>();
        final ArrayList<String> employeeNames = new ArrayList<>();
        final ArrayList<DaysOff> daysOff = new ArrayList<>();

        /*
          getting employee days off data from database
          This code only stores days off data of the particular date chosen
          which was passed from the intent
         */
        mDaysOffDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DaysOff dayOff = dataSnapshot.getValue(DaysOff.class);
                if(DATE_FORMAT.format(dayOff.getDate()).equals(date))
                {
                    daysOff.add(dayOff);
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


        /*
         adding employees from database to the array list
         The code only add employees who did not request day off on the date
         */
        mDatabase.addChildEventListener(new ChildEventListener() {
            int i = 1;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Employee value = dataSnapshot.getValue(Employee.class);
                if(daysOff.size() != 0)
                {
                    boolean foundDaysOff = false;
                    DaysOff foundEmployee = new DaysOff();
                    for(DaysOff temp : daysOff)
                    {
                        boolean repeated = false;
                        if(!temp.getEmployeeEmail().equalsIgnoreCase(value.getEmail()))
                        {
                            for(Employee e : employees)
                            {
                                if(e.getEmail().equalsIgnoreCase(value.getEmail()))
                                {
                                    repeated = true;
                                }
                            }
                            if(!repeated)
                            {
                                employees.add(value);
                                employeeNames.add(i+" " + value.getFullName());
                                i++;
                            }
                        }
                        else
                        {
                            foundEmployee = temp;
                            foundDaysOff = true;
                            break;
                        }
                    }
                    if(foundDaysOff)
                    {
                        daysOff.remove(foundEmployee);
                    }

                }
                else
                {
                    employees.add(value);
                    employeeNames.add(i+" " + value.getFullName());
                    i++;
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


        premiseLabel.setText(premise);
        dateLabel.setText(date);

        /*
        Remove all timeslot fields in the activity
         */
        removeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout contentLayout = (LinearLayout) findViewById(R.id.middleContent);
                int childCount = contentLayout.getChildCount();
                if(childCount > 0)
                {
                    contentLayout.removeAllViews();
                }
            }
        });

        /*
        add one timeslot to the activity
         */
        addField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffNum++;
                Spinner staff = new Spinner(getApplicationContext());

                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_item, employeeNames){

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                // TODO Auto-generated method stub

                                View view = super.getView(position, convertView, parent);

                                TextView text = (TextView)view.findViewById(android.R.id.text1);
                                text.setTextColor(getResources().getColor(R.color.colorPrimary));

                                return view;

                            }
                        };

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                staff.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();

                TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                staff.setLayoutParams(params);
                EditText startTime = new EditText(getApplicationContext());
                startTime.setLayoutParams(params);
                startTime.setInputType(InputType.TYPE_CLASS_DATETIME);

                startTime.setHint("HH:MM");
                startTime.setTextColor(getResources().getColor(R.color.colorPrimary));
                startTime.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                EditText endTime = new EditText(getApplicationContext());
                endTime.setLayoutParams(params);
                endTime.setInputType(InputType.TYPE_CLASS_DATETIME);
                endTime.setHint("HH:MM");
                endTime.setTextColor(getResources().getColor(R.color.colorPrimary));
                endTime.setHintTextColor(getResources().getColor(R.color.colorPrimary));

                LinearLayout newContent = new LinearLayout(getApplicationContext());
                newContent.setOrientation(LinearLayout.HORIZONTAL);
                newContent.setId(staffNum);

                newContent.addView(startTime);
                newContent.addView(endTime);
                newContent.addView(staff);
                content.addView(newContent);
            }
        });

        /*
        Confirming the roster and storing it into the database
         */
        addRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout contentLayout = (LinearLayout) findViewById(R.id.middleContent);
                int childCount = contentLayout.getChildCount();
                rosteredEmployee.clear();
                roster = new Roster();
                if(contentLayout.getChildCount() == 0)
                {
                    Toast.makeText(getApplicationContext(),
                            "You need to add at least one timeslot", Toast.LENGTH_LONG).show();
                    return;
                }
                for(int i = 0; i < childCount; i++)
                {
                    Timeslot timeslot = new Timeslot();
                    String startTime = new String();
                    String endTime = new String();
                    EditText startTimeInput;
                    EditText endTimeInput;
                    LinearLayout content = (LinearLayout) contentLayout.getChildAt(i);

                    int employeeIndex = 0;
                    int fields = content.getChildCount();
                    for(int j = 0; j < fields; j++)
                    {
                        if(j == 0)
                        {
                            startTimeInput = (EditText) content.getChildAt(j);
                            startTime = startTimeInput.getText().toString();
                            //add regex to check time input
                        }
                        else if(j == 1)
                        {
                            endTimeInput = (EditText) content.getChildAt(j);
                            endTime = endTimeInput.getText().toString();
                            if(startTime.equals(endTime))
                            {
                                Toast.makeText(getApplicationContext(),
                                        "You cannot have same start and end time", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        else if(j == 2)
                        {
                            Spinner spinner = (Spinner) content.getChildAt(j);
                            String employeeName = spinner.getSelectedItem().toString();
                            String index = employeeName.replaceAll("[^0-9]", "");
                            employeeIndex = Integer.parseInt(index);
                            if(rosteredEmployee.isEmpty())
                            {
                                rosteredEmployee.add(employeeIndex);
                                timeslot.setEmployee(employees.get(employeeIndex-1));
                                timeslot.setStartTime(startTime);
                                timeslot.setEndTime(endTime);
                            }
                            else
                            {
                                boolean noEmployeeRepeat = false;
                                for(int rosteredIndex : rosteredEmployee)
                                {
                                    if(rosteredIndex == employeeIndex)
                                    {
                                        Toast.makeText(getApplicationContext(),
                                                "You cannot have duplicate employee on different time slot",
                                                    Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else
                                    {
                                        noEmployeeRepeat = true;
                                    }
                                }
                                if(noEmployeeRepeat)
                                {
                                    rosteredEmployee.add(employeeIndex);
                                    timeslot.setEmployee(employees.get(employeeIndex-1));
                                    timeslot.setStartTime(startTime);
                                    timeslot.setEndTime(endTime);

                                }
                            }
                        }
                    }
                    Date date = calendar.getTime();
                    roster.setDate(date);
                    roster.setPremise(premise);
                    roster.addTimeslot(timeslot);
                }

                mRosterDatabase.push().setValue(roster).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete())
                        {
                            Toast.makeText(getApplicationContext(), "Data Successfully Stored", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), AdminRoster.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Failed to store data.. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
