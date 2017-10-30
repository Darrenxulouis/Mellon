package com.mad.mellon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * This class allows admin users to add premise
 * premise is a business location where the staff will operate
 * Created by Darren on 1/06/2017.
 */

public class AdminPremise extends AppCompatActivity{
    private ArrayList<String> premiseName = new ArrayList<>();
    private EditText premiseInput;
    private DatabaseReference mDatabase;
    private static final String PREMISE = "Premise";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_premise);
        premiseInput = (EditText) findViewById(R.id.addPremiseInput);
        Button premiseInputButton = (Button) findViewById(R.id.addPremiseButton);
        ListView premiseList = (ListView) findViewById(R.id.premiseListView);
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PREMISE);

        /*
        Configuring adapter for the premise
         */
        final ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, premiseName);
        premiseList.setAdapter(arrayAdapter);

        /*
        getting all premise data from the database and populating in the arraylist
        The array list will be used to show in the page through adapter
         */
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String premise = dataSnapshot.getValue(String.class);
                premiseName.add(premise);
                arrayAdapter.notifyDataSetChanged();
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
        The button will invoke checking of the user input for the premise
        If the name of the premise is logical, it will add to the database
         */
        premiseInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String premise = premiseInput.getText().toString().trim();
                String checkPremise = premise.replace(" ", "");
                if(checkPremise.matches("[A-Za-z0-9-]+[ 0-9A-Za-z#$%=@!{},`~&*()'<>?.:;_|^/+\\t\\r\\n\\[\\]\"-]*"))
                {
                    mDatabase.push().setValue(premise);
                    Toast.makeText(getApplicationContext(),
                            "Successfully added premise",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Please input the correct premise name",
                            Toast.LENGTH_LONG).show();
                }

            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_bottom);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
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
                        Intent intent1 = new Intent(AdminPremise.this, AdminMainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.staff:
                        Intent intent2 = new Intent(AdminPremise.this, AdminStaff.class);
                        startActivity(intent2);
                        break;
                    case R.id.roster:
                        Intent intent3 = new Intent(AdminPremise.this, AdminRoster.class);
                        startActivity(intent3);
                        break;
                    case R.id.premise:

                        break;
                    case R.id.account:
                        Intent intent4 = new Intent(AdminPremise.this, AdminAccount.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }
}
