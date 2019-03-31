package com.example.biddlr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import classes.DatabaseManager;
import classes.Job;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<Job>> listDataChild;

    public ExpandableListAdapter(Context con, List<String> headers, HashMap<String, List<Job>> data){
        context = con;
        listDataHeader = headers;
        listDataChild = data;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int pos) {
        return listDataChild.get(listDataHeader.get(pos)).size();
    }

    @Override
    public Object getGroup(int pos) {
        return listDataHeader.get(pos);
    }

    @Override
    public Object getChild(int groupPos, int childPos) {
        return listDataChild.get(groupPos).get(childPos);
    }

    @Override
    public long getGroupId(int pos) {
        return pos;
    }

    @Override
    public long getChildId(int groupPos, int childPos) {
        return childPos;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int pos, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(pos);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.collapsable_header, null);
        }

        TextView txtHeader = convertView.findViewById(R.id.txtHeader);
        txtHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView, ViewGroup parent) {
        Job job = listDataChild.get(listDataHeader.get(groupPos)).get(childPos);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.job_list_item, null);
        }

        ImageView imgJobPic = convertView.findViewById(R.id.imgJobPic);
        DatabaseManager.shared.setImage(job.getJobID(), imgJobPic);

        TextView txtJobTitle = convertView.findViewById(R.id.txtJobTitle);
        txtJobTitle.setText(job.getTitle());

        TextView txtJobLocation = convertView.findViewById(R.id.txtJobLocation);
        txtJobLocation.setText(job.getLocation());

        TextView txtTimeRemaining = convertView.findViewById(R.id.txtTimeRemaining);
        String time = timeLeft(job.getExpirationDate().toLocalDateTime());
        txtTimeRemaining.setText(time);

        TextView txtStartingPrice = convertView.findViewById(R.id.txtStartingPrice);
        String price = "$" + String.format("%.2f", job.getStartingPrice());
        txtStartingPrice.setText(price);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private String timeLeft(LocalDateTime expDate){
        if(expDate.isBefore(LocalDateTime.now())){
            return "Expired";
        }

        String unit = "min";
        long time = LocalDateTime.now().until(expDate, ChronoUnit.MINUTES);

        if(time > 60){
            unit = "hrs";
            time /= 60;
            if(time > 24){
                unit = "days";
                time /= 24;
            }
        }

        return time + " " + unit;
    }
}
