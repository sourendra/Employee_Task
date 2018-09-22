package com.sourendra.employeetask;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourendra.employeetask.adapter.LeaveAdapter;
import com.sourendra.employeetask.model.LeaveDetails;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class EmployeeDetails extends AppCompatActivity {
    ImageView ivEmployeePhoto;
    TextView tvEmployeeName;
    TextView tvEmployeeAge;
    TextView tvEmployeeGender;
    TextView tvEmployeeDesignation;
    TextView tvSanctionedLeaves;
    NonScrollListView nsvSanctionedLeaves;
    Spinner spOptions;
    EditText etDate;
    LinearLayout llDateRange;
    EditText etFromDate;
    EditText etToDate;
    Button btnSanction;

    String employeeId = "";
    String designation = "";
    String name = "";
    String age = "";
    String employeeCode = "";
    String region = "";
    String gender = "";
    String imageUrl = "";

    ArrayList<HashMap<String, String>> alLeaveOptions = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ArrayList<LeaveDetails> alLeaveDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        //Creating sharedpreferences to store data, so we can use it in future.
        sharedPreferences = getSharedPreferences("Leave Details", MODE_PRIVATE);

        //To get data from previous Activity we use Bundle. Here we are getting the data.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().getStringExtra("employeeId") != null) {
                employeeId = bundle.getString("employeeId");
            }
            if (getIntent().getStringExtra("designation") != null) {
                designation = bundle.getString("designation");
            }
            if (getIntent().getStringExtra("name") != null) {
                name = bundle.getString("name");
            }
            if (getIntent().getStringExtra("age") != null) {
                age = bundle.getString("age");
            }
            if (getIntent().getStringExtra("employeeCode") != null) {
                employeeCode = bundle.getString("employeeCode");
            }
            if (getIntent().getStringExtra("region") != null) {
                region = bundle.getString("region");
            }
            if (getIntent().getStringExtra("gender") != null) {
                gender = bundle.getString("gender");
            }
            if (getIntent().getStringExtra("imageUrl") != null) {
                imageUrl = bundle.getString("imageUrl");
            }
        }

        //Creating data for Leave options, since i am not getting data from any web service or url.
        HashMap<String, String> map1 = new HashMap<>();
        map1.put("id", "1");
        map1.put("option", "Full Day");
        alLeaveOptions.add(map1);
        HashMap<String, String> map2 = new HashMap<>();
        map2.put("id", "2");
        map2.put("option", "Half Day");
        alLeaveOptions.add(map2);
        HashMap<String, String> map3 = new HashMap<>();
        map3.put("id", "3");
        map3.put("option", "More Than One Day");
        alLeaveOptions.add(map3);

        //Initializing all view elements which are used in this Activity by calling this method.
        initLayout();

        //Setting data provided by previous Activity to the view Elements.
        setData();
    }

    private void initLayout() {
        ivEmployeePhoto = findViewById(R.id.employeeDetails_ivEmployee);
        tvEmployeeName = findViewById(R.id.employeeDetails_tvEmployee);
        tvEmployeeAge = findViewById(R.id.employeeDetails_tvAge);
        tvEmployeeGender = findViewById(R.id.employeeDetails_tvGender);
        tvEmployeeDesignation = findViewById(R.id.employeeDetails_tvDesignation);
        tvSanctionedLeaves = findViewById(R.id.employeeDetails_tvSanctionedLeaves);
        nsvSanctionedLeaves = findViewById(R.id.employeeDetails_nsSanctionsLeaves);
        spOptions = findViewById(R.id.employeeDetails_spLeaveOptions);
        etDate = findViewById(R.id.employeeDetails_etDate);
        llDateRange = findViewById(R.id.employeeDetails_llDateRange);
        etFromDate = findViewById(R.id.employeeDetails_etFromDate);
        etToDate = findViewById(R.id.employeeDetails_etToDate);
        btnSanction = findViewById(R.id.employeeDetails_btnSanction);

        SimpleAdapter saOptions = new SimpleAdapter(EmployeeDetails.this, alLeaveOptions, android.R.layout.simple_dropdown_item_1line, new String[]{"option"}, new int[]{android.R.id.text1});
        spOptions.setAdapter(saOptions);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etDate);
            }
        });

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etFromDate);
            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate(etToDate);
            }
        });

        btnSanction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValidated = false;
                String option = alLeaveOptions.get(spOptions.getSelectedItemPosition()).get("option");
                String optionId = alLeaveOptions.get(spOptions.getSelectedItemPosition()).get("id");
                String date = "";
                if (optionId.equals("3")) {
                    String fromDate = etFromDate.getText().toString();
                    String toDate = etToDate.getText().toString();
                    Date from = null;
                    Date to = null;
                    try {
                        from = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fromDate);
                        to = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(toDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if ((fromDate.equals("") && fromDate.length() <= 0) && (toDate.equals("") && toDate.length() <= 0)) {
                        Toast.makeText(EmployeeDetails.this, "Select From Date and To Date!", Toast.LENGTH_SHORT).show();
                        isValidated = false;
                    } else if (fromDate.equals("") && fromDate.length() <= 0) {
                        Toast.makeText(EmployeeDetails.this, "Select From Date!", Toast.LENGTH_SHORT).show();
                        isValidated = false;
                    } else if (toDate.equals("") && toDate.length() <= 0) {
                        Toast.makeText(EmployeeDetails.this, "Select To Date!", Toast.LENGTH_SHORT).show();
                        isValidated = false;
                    } else if (from != null && to != null && (from.after(to) || from.compareTo(to) == 0)) {
                        Toast.makeText(EmployeeDetails.this, "Invalid Date!", Toast.LENGTH_SHORT).show();
                        isValidated = false;
                    } else {
                        date = fromDate + "-" + toDate;
                        isValidated = true;
                    }
                } else {
                    llDateRange.setVisibility(View.GONE);
                    etDate.setVisibility(View.VISIBLE);
                    date = etDate.getText().toString();
                    if (date.equals("")) {
                        Toast.makeText(EmployeeDetails.this, "Select Date!", Toast.LENGTH_SHORT).show();
                        isValidated = false;
                    } else {
                        isValidated = true;
                    }
                }
                if (isValidated) {
                    if (alLeaveDetails != null && alLeaveDetails.size() > 0) {
                        HashSet<String> empIds = new HashSet<>();
                        for (LeaveDetails leaveDetails : alLeaveDetails) {
                            empIds.add(leaveDetails.getEmployeeId());
                        }
                        if (empIds.contains(employeeId)) {
                            for (LeaveDetails leaveDetails : alLeaveDetails) {
                                if (leaveDetails.getEmployeeId().equals(employeeId)) {
                                    ArrayList<HashMap<String, String>> leaves = leaveDetails.getAlLeaves();
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("date", date);
                                    map.put("option", option);
                                    leaves.add(map);
                                    storeDataToPrefs();
                                    retrieveDataFromPrefs();
                                }
                            }
                        } else {
                            addNewLeave(date, option);
                        }
                    } else {
                        alLeaveDetails = new ArrayList<>();
                        addNewLeave(date, option);
                    }
                    Toast.makeText(EmployeeDetails.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                    etDate.setText("");
                    etFromDate.setText("");
                    etToDate.setText("");
                }
            }
        });

        //If leave option is more than one day the Date range will be there otherwise single Date selection.
        spOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String optionId = alLeaveOptions.get(spOptions.getSelectedItemPosition()).get("id");
                if (optionId.equals("3")) {
                    llDateRange.setVisibility(View.VISIBLE);
                    etDate.setVisibility(View.GONE);
                } else {
                    llDateRange.setVisibility(View.GONE);
                    etDate.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Add new Sanction Leave for the Employee.
    private void addNewLeave(String date, String option) {
        ArrayList<HashMap<String, String>> leaves = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("date", date);
        map.put("option", option);
        leaves.add(map);
        LeaveDetails leaveDetails = new LeaveDetails(employeeId, leaves);
        alLeaveDetails.add(leaveDetails);
        storeDataToPrefs();
        retrieveDataFromPrefs();
    }

    private void setData() {
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_person).error(R.drawable.ic_person).into(ivEmployeePhoto);
        tvEmployeeName.setText(name);
        tvEmployeeAge.setText(age);
        tvEmployeeGender.setText(gender);
        tvEmployeeDesignation.setText(designation);
        retrieveDataFromPrefs();
    }

    //Strore data to the SharedPreferences using Gson because we can't use any particular object into the SharedPreferences.
    private void storeDataToPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String data = gson.toJson(alLeaveDetails);
        editor.putString("leaveList", data);
        editor.apply();
    }

    //Retrieve stored data in SharedPreferences.
    private void retrieveDataFromPrefs() {
        Gson gson = new Gson();
        String data = sharedPreferences.getString("leaveList", null);
        Type type = new TypeToken<ArrayList<LeaveDetails>>() {
        }.getType();
        alLeaveDetails = gson.fromJson(data, type);
        if (alLeaveDetails != null && alLeaveDetails.size() > 0) {
            for (LeaveDetails leaveDetails : alLeaveDetails) {
                if (leaveDetails.getEmployeeId().equals(employeeId)) {
                    tvSanctionedLeaves.setVisibility(View.VISIBLE);
                    nsvSanctionedLeaves.setVisibility(View.VISIBLE);
                    ArrayList<HashMap<String, String>> alLeaves = leaveDetails.getAlLeaves();
                    LeaveAdapter leaveAdapter = new LeaveAdapter(EmployeeDetails.this, alLeaves, R.layout.leave_cell);
                    nsvSanctionedLeaves.setAdapter(leaveAdapter);
                    break;
                } else {
                    tvSanctionedLeaves.setVisibility(View.GONE);
                    nsvSanctionedLeaves.setVisibility(View.GONE);
                }
            }
        } else {
            tvSanctionedLeaves.setVisibility(View.GONE);
            nsvSanctionedLeaves.setVisibility(View.GONE);
        }
    }

    //To get the data selected by the user on Calendar.
    private void selectDate(final EditText etField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                etField.setText(String.format("%02d", day) + "/" + String.format("%02d", (month + 1)) + "/" + year);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeDetails.this, dateSetListener, year, month, day);
        datePickerDialog.show();

    }
}
