package com.mad.mellon;

import java.util.ArrayList;
import java.util.Date;

/**
 * Roster model class. This class contains premise name, date, and list of employees rostered
 * Created by Darren on 2/06/2017.
 */

public class Roster {
    private String premise;
    private Date date;
    private ArrayList<Timeslot> roster = new ArrayList<>();


    public String getPremise() {
        return premise;
    }

    public void setPremise(String premise) {
        this.premise = premise;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Timeslot> getRoster() {
        return roster;
    }

    public void setRoster(ArrayList<Timeslot> roster) {
        this.roster = roster;
    }

    public void addTimeslot(Timeslot timeslot)
    {
        roster.add(timeslot);
    }
}
