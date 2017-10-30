package com.mad.mellon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * This is the adapter class for roster
 * Created by Darren on 2/06/2017.
 */

public class RosterAdapter extends RecyclerView.Adapter<RosterAdapter.ViewHolder> {
    private FirebaseAuth mAuth;
    private Context context;
    private ArrayList<Roster> rosters = new ArrayList<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy - MMM - dd");
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("EEEE");

    public RosterAdapter(Context context, ArrayList<Roster> rosters)
    {
        this.context = context;
        this.rosters = rosters;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public RosterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.roster_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RosterAdapter.ViewHolder holder, final int position) {
        Roster roster = rosters.get(position);
        String date = DATE_FORMAT.format(roster.getDate());
        String day = DAY_FORMAT.format(roster.getDate());
        holder.rosterDate.setText(date);
        holder.rosterDay.setText(day);
        holder.rosterPremise.setText(roster.getPremise());

        /*
        executing roster tap on click of roster row. Roster Tap is an async task
         */
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RosterTap(position, context).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return rosters.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rosterDate, rosterDay, rosterPremise;
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            rosterDate = (TextView) itemView.findViewById(R.id.rosterDate);
            rosterDay = (TextView) itemView.findViewById(R.id.rosterDay);
            rosterPremise = (TextView) itemView.findViewById(R.id.rosterPremise);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.rosterLayout);
        }
    }


    /*
    Async Task that redirects users to the roster corresponding details on tap
     */
    private class RosterTap extends AsyncTask<Void, Void, Void>
    {
        private Context context;
        private int position;
        private Roster roster = new Roster();
        private static final String ADMIN_LOGIN = "admin@admin.com";
        private static final String PREMISE = "premise";
        private static final String YEAR = "year";
        private static final String MONTH = "month";
        private static final String DAY = "day";
        RosterTap(int position, Context context)
        {
            this.position = position;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            roster = rosters.get(position);
            return null;
        }

        /*
        redirecting to the type of roster details page based on logged in user
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent;
            if(mAuth.getCurrentUser().getEmail().toLowerCase().equals(ADMIN_LOGIN))
            {
                intent = new Intent(context, AdminRosterDetails.class);
            }
            else
            {
                intent = new Intent(context, EmployeeRosterDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            String premise = roster.getPremise();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(roster.getDate());

            intent.putExtra(PREMISE, premise);
            intent.putExtra(YEAR, calendar.get(Calendar.YEAR));
            intent.putExtra(MONTH, calendar.get(Calendar.MONTH));
            intent.putExtra(DAY, calendar.get(Calendar.DAY_OF_MONTH));
            context.startActivity(intent);

        }
    }
}
