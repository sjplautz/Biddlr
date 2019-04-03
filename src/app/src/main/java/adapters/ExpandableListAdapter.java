package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.biddlr.R;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

import classes.DatabaseManager;
import classes.Job;

/**
 * An adapter for the collapsible list describing a user's jobs
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<Job>> listDataChild;

    public ExpandableListAdapter(Context con, List<String> headers, HashMap<String, List<Job>> data){
        context = con;
        listDataHeader = headers;
        listDataChild = data;
    }

    /**
     * Gets count of collapsible groups
     * @return The number of collapsible groups
     */
    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    /**
     * Gets count of children in a group
     * @param pos The group index
     * @return The number of children in a given group
     */
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
        return true;
    }

    /**
     * Describes the ui for displaying a group
     * @param pos The index of the group
     * @param isExpanded True if group is expanded
     * @param convertView The view describing the group
     * @param parent
     * @return A View describing the ui for the group
     */
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

    /**
     * Describes the ui for displaying the list items within a group
     * @param groupPos The index of the group
     * @param childPos The index of the child within the group
     * @param isLastChild True if last child in a group
     * @param convertView The view describing the list item
     * @param parent
     * @return A View describing the ui for a list item within a group
     */
    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView, ViewGroup parent) {
        Job job = listDataChild.get(listDataHeader.get(groupPos)).get(childPos);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_child_item, null);
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

        return convertView;
    }

    /**
     * Describes if a list item is selectable
     * @param groupPosition The index of the group
     * @param childPosition The index of the child within the group
     * @return True if the item is selectable
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * Determines how much time is left before a job expires
     * @param expDate The expiration date of the job
     * @return A string representation of the time remaining
     */
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
