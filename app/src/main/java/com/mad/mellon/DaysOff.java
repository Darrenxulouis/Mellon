package com.mad.mellon;

import java.util.Date;

/**
 * Model class of day off that accepts employee email as unique identifier and date of requested day off
 * Created by Darren on 30/05/2017.
 */

public class DaysOff {
    private String employeeEmail;
    private Date date;

    public DaysOff()
    {

    }


    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
