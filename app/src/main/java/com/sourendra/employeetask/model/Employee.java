package com.sourendra.employeetask.model;

import android.util.Log;

public class Employee {

    private final String id;
    private final String name;
    private final String age;
    private final String designation;
    private final String region;
    private final String employeeCode;
    private final String gender;
    private final String imageUrl;

    public Employee(String id, String name, String age, String designation, String region, String employeeCode, String gender, String imageUrl){

        this.id = id;
        this.name = name;
        this.age = age;
        this.designation = designation;
        this.region = region;
        this.employeeCode = employeeCode;
        this.gender = gender;
        this.imageUrl = imageUrl;
        Log.e("response-->", "==="+id+","+name+","+age+","+designation+","+region+","+employeeCode+","+imageUrl);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getDesignation() {
        return designation;
    }

    public String getRegion() {
        return region;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getGender() {
        return gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
