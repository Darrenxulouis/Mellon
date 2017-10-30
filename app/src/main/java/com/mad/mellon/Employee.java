package com.mad.mellon;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Employee model class that takes email as unique identifier in database and full name of the
 * employee
 * Created by Darren on 28/05/2017.
 */

public class Employee {
    private String fullName;
    private String email;

    public Employee()
    {

    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String newFullName)
    {
        fullName = newFullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
