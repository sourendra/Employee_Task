package com.sourendra.employeetask.model;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaveDetails {

    private final String employeeId;
    private final ArrayList<HashMap<String, String>> alLeaves;

    public LeaveDetails(String employeeId, ArrayList<HashMap<String, String >> alLeaves){

        this.employeeId = employeeId;
        this.alLeaves = alLeaves;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public ArrayList<HashMap<String, String>> getAlLeaves() {
        return alLeaves;
    }
}
