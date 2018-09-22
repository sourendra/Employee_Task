package com.sourendra.employeetask.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sourendra.employeetask.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LeaveAdapter extends ArrayAdapter {

    private ArrayList<HashMap<String, String>> dataSet;
    Context mContext;
    int layoutId;

    // View lookup cache
    static class ViewHolder {
        TextView tvId;
        TextView tvDate;
        TextView tvOption;
    }

    public LeaveAdapter(Context context, ArrayList<HashMap<String, String>> data, int layoutId) {
        super(context, layoutId, data);
        this.dataSet = data;
        this.mContext = context;
        this.layoutId = layoutId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HashMap<String, String> dataModel = (HashMap<String, String>) dataSet.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

//        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutId, parent, false);
            viewHolder.tvId = (TextView) convertView.findViewById(R.id.leaveCell_tvId);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.leaveCell_tvDate);
            viewHolder.tvOption = (TextView) convertView.findViewById(R.id.leaveCell_tvOption);

//            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            result=convertView;
        }
        Log.d("map-->", "==========" + dataModel);
        viewHolder.tvId.setText(String.valueOf(position + 1));
        viewHolder.tvDate.setText(dataModel.get("date"));
        viewHolder.tvOption.setText(dataModel.get("option"));
        /*viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);*/
        // Return the completed view to render on screen
        return convertView;
    }
}