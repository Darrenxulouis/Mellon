package com.mad.mellon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Adapter class for days off
 * Created by Darren on 30/05/2017.
 */

public class DaysOffAdapter extends RecyclerView.Adapter<DaysOffAdapter.ViewHolder> {
    private DatabaseReference mDatabase;
    private Context context;
    private ArrayList<DaysOff> daysOffs;
    private ArrayList<String> keys;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy - MMM - dd");
    private static final SimpleDateFormat DAY_FORMAT =  new SimpleDateFormat("EEEE");
    private static final String DAYS_OFF = "DaysOff";

    /**
     * Getting context, days off array and keys to be used for showing
     */
    public DaysOffAdapter(Context context, ArrayList<DaysOff> daysOffs, ArrayList<String>keys)
    {
        this.context = context;
        this.daysOffs = daysOffs;
        this.keys = keys;
        mDatabase = FirebaseDatabase.getInstance().getReference().child(DAYS_OFF);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.requested_day_off_item, parent, false);
        return new ViewHolder(itemView);
    }

    /*
    Binding all the views to front end
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DaysOff daysOff = daysOffs.get(position);
            String dateString = DATE_FORMAT.format(daysOff.getDate());
            String dayString = DAY_FORMAT.format(daysOff.getDate());

            holder.requestedDate.setText(dateString);
            holder.requestedDay.setText(dayString);
            holder.removeRequestedDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            new removeRequestedDayOff(holder,position, context).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysOffs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView requestedDate, requestedDay;
        private Button removeRequestedDate;
        public ViewHolder(View itemView) {
            super(itemView);
            requestedDate = (TextView) itemView.findViewById(R.id.requestedDate);
            requestedDay = (TextView) itemView.findViewById(R.id.requestedDay);
            removeRequestedDate = (Button) itemView.findViewById(R.id.removeRequestedButton);
        }
    }

    /**
     * Async Task that is used to remove days of that has been requested on click
     */
    class removeRequestedDayOff extends AsyncTask<Void, Void, Void>
    {
        ViewHolder holder;
        private int position;
        final String tempKey = keys.get(position);
        Context context;

        public removeRequestedDayOff(DaysOffAdapter.ViewHolder holder, int position, Context context)
        {
            this.holder = holder;
            this.position = position;
            this.context = context;
        }


        /**
         * Getting information from database and removing based on the button on the row that is clicked
         */
        @Override
        protected Void doInBackground(Void... params) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mDatabase.child(tempKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            daysOffs.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        /**
         * start activity to return back to schedule
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            context.startActivity(new Intent(context, Schedule.class));
        }
    }
}
