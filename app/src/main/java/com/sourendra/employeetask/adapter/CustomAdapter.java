package com.sourendra.employeetask.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sourendra.employeetask.OnListItemClickListener;
import com.sourendra.employeetask.R;
import com.sourendra.employeetask.model.Employee;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    OnListItemClickListener onListItemClickListener;
    private List<Employee> employeeList;

    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView countryText;
        public ImageView ivEmployeePhoto;
        public LinearLayout llCell;

        public MyViewHolder(View view) {
            super(view);
            countryText = (TextView) view.findViewById(R.id.employeeCell_tvEmployeeName);
            ivEmployeePhoto = (ImageView) view.findViewById(R.id.employeeCell_ivEmployeeImage);
            llCell = (LinearLayout) view.findViewById(R.id.employeeCell_llCell);
        }
    }

    public CustomAdapter(List<Employee> employeeList, OnListItemClickListener onListItemClickListener) {
        this.employeeList = employeeList;
        this.onListItemClickListener = onListItemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Employee employee = employeeList.get(position);
        holder.countryText.setText(employee.getName());
        Picasso.get()
                .load(employee.getImageUrl())
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_person)
                .into(holder.ivEmployeePhoto);

        holder.llCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onListItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_cell,parent, false);
        return new MyViewHolder(v);
    }
}
