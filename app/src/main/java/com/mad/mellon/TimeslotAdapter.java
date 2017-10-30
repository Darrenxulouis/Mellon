package com.mad.mellon;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Timeslot adapter to help showing timeslots used by recycler view
 * Created by Darren on 3/06/2017.
 */

public class TimeslotAdapter extends RecyclerView.Adapter<TimeslotAdapter.ViewHolder> {
    private ArrayList<Timeslot> timeslots = new ArrayList<>();
    public TimeslotAdapter(ArrayList<Timeslot> timeslots)
    {
        this.timeslots = timeslots;
    }

    @Override
    public TimeslotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.roster_timeslot_item, parent, false);
        return new ViewHolder(itemView);
    }

    /*
    Binding the variables to recycler views
     */
    @Override
    public void onBindViewHolder(TimeslotAdapter.ViewHolder holder, int position) {
        Timeslot timeslot = timeslots.get(position);
        holder.startTime.setText(timeslot.getStartTime());
        holder.endTime.setText(timeslot.getEndTime());
        holder.employeeName.setText(timeslot.getEmployee().getFullName());
    }

    @Override
    public int getItemCount() {
        return timeslots.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView startTime, endTime, employeeName;

        public ViewHolder(View itemView) {
            super(itemView);
            startTime = (TextView) itemView.findViewById(R.id.rosterStartTime);
            endTime = (TextView) itemView.findViewById(R.id.rosterEndTime);
            employeeName = (TextView) itemView.findViewById(R.id.rosterName);

        }
    }
}
