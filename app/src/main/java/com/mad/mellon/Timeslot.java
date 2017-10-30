package com.mad.mellon;

import java.util.ArrayList;

/**
 * Timeslot model that contains start time, end time, and employee object to the particular timeslot
 * Created by Darren on 2/06/2017.
 */

public class Timeslot {
    private String startTime;
    private String endTime;
    private Employee employee;


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
