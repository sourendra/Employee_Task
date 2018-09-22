package com.sourendra.employeetask;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sourendra.employeetask.adapter.CustomAdapter;
import com.sourendra.employeetask.model.Employee;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnListItemClickListener {

    RecyclerView rvEmployeeList;
    ProgressBar progressBar;
    JsonObjectRequest jsonObjectRequest;
    ArrayList<Employee> alEmployee = new ArrayList<>();
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //Initializing all view elements which are used in this Activity by calling this method.
        initLayout();

        //Sending request to particular url to get data from that.
        sendRequest();

    }

    private void initLayout(){
        progressBar = findViewById(R.id.main_progress);
        rvEmployeeList = (RecyclerView) findViewById(R.id.main_rvEmployeeList);
        customAdapter = new CustomAdapter(alEmployee, this);

        //I am showing data in vertically & linearly in list so for that i am setting LinearLayoutManager to the RecyclerView.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvEmployeeList.setLayoutManager(linearLayoutManager);

        //Here i have fixed size of data so i have defined it as true.
        rvEmployeeList.setHasFixedSize(true);

        rvEmployeeList.setItemViewCacheSize(20);
        rvEmployeeList.setDrawingCacheEnabled(true);

        //Added divider to the RecyclerView by using addItemDecoration.
        rvEmployeeList.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider)));
    }

    private void sendRequest(){
        //This is the url from where i will get data.
        String url = "https://spreadsheets.google.com/feeds/list/1Bi29kPVZgfRuTgnv8OaXMXk-55kKHH4g2F4CkMPhQNI/od6/public/values?alt=json";

        //As the response is a JSONObject so i am using JsonObjectRequest provided by Volley.
        jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = response.getJSONObject("feed");
                    JSONArray entry = jsonObject.getJSONArray("entry");
                    for (int i = 0; i < entry.length(); i++) {
                        JSONObject populateObject = entry.getJSONObject(i);
                        JSONObject gsx$employeeid = populateObject.getJSONObject("gsx$employeeid");
                        String id = gsx$employeeid.getString("$t");
                        JSONObject gsx$designation = populateObject.getJSONObject("gsx$designation");
                        String designation = gsx$designation.getString("$t");
                        JSONObject gsx$name = populateObject.getJSONObject("gsx$name");
                        String name = gsx$name.getString("$t");
                        JSONObject gsx$age = populateObject.getJSONObject("gsx$age");
                        String age = gsx$age.getString("$t");
                        JSONObject gsx$employeecode = populateObject.getJSONObject("gsx$employeecode");
                        String employeecode = gsx$employeecode.getString("$t");
                        JSONObject gsx$region = populateObject.getJSONObject("gsx$region");
                        String region = gsx$region.getString("$t");
                        JSONObject gsx$gender = populateObject.getJSONObject("gsx$gender");
                        String gender = gsx$gender.getString("$t");
                        JSONObject gsx$imageurl = populateObject.getJSONObject("gsx$imageurl");
                        String imageurl = gsx$imageurl.getString("$t");

                        Employee employee = new Employee(id, name, age, designation, region, employeecode, gender, imageurl);
                        alEmployee.add(employee);
                    }
                    customAdapter.notifyDataSetChanged();
                    showDatainList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                //If any error occurs then this toast message will be shown.
                Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        //Setting request policy for the request.
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding this request to the Volley.
        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);
    }

    //After getting data it will be shown into to RecyclerView by using this CustomAdapter
    private void showDatainList() {
        rvEmployeeList.setAdapter(customAdapter);
    }

    //On clicking to the list item cell it will go to EmployeeDetails Activity. There i am passing all required data.
    @Override
    public void onItemClick(int cellId) {
        Employee employee = alEmployee.get(cellId);
        Intent intent = new Intent(MainActivity.this, EmployeeDetails.class);
        intent.putExtra("employeeId", employee.getId());
        intent.putExtra("designation", employee.getDesignation());
        intent.putExtra("name", employee.getName());
        intent.putExtra("age", employee.getAge());
        intent.putExtra("employeeCode", employee.getEmployeeCode());
        intent.putExtra("region", employee.getRegion());
        intent.putExtra("gender", employee.getGender());
        intent.putExtra("imageUrl", employee.getImageUrl());
        startActivity(intent);
    }

    //If user close the application then this Activity will be stopped or destroyed. So to handle memory leak i am cancelling the JsonObjectRequest.
    @Override
    protected void onStop() {
        jsonObjectRequest.cancel();
        super.onStop();
    }
}
