package com.example.biddlr;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.List;

import classes.Job;


public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {
    private List<Job> jobsList;

    class JobViewHolder extends RecyclerView.ViewHolder{
        private ImageView jobPicture;
        private TextView jobTitle;
        private TextView jobPrice;
        private TextView jobLocation;
        private TextView jobTime;

        private JobViewHolder(View v) {
            super(v);
            jobPicture = v.findViewById(R.id.imgJobPic);
            jobTitle = v.findViewById(R.id.txtJobTitle);
            jobPrice = v.findViewById(R.id.txtStartingPrice);
            jobLocation = v.findViewById(R.id.txtJobLocation);
            jobTime = v.findViewById(R.id.txtTimeRemaining);
        }
    }

    public JobListAdapter(List<Job> jobsList){
        this.jobsList = jobsList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup v, int viewType){
        View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.job_list_item, v, false);
        return new JobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int pos){
        Job job = jobsList.get(pos);

        //Set up job picture
        int picId;
        picId = job.getPhotoURL().equals("job1") ? R.drawable.job1 : R.drawable.job2;
        holder.jobPicture.setImageResource(picId);

        //Set job title
        holder.jobTitle.setText(job.getTitle());

        //Set job price
        String price = "$" + job.getStartingPrice();
        holder.jobPrice.setText(price);

        //Set job location
        if(job.getLocation() != null)
            holder.jobLocation.setText(job.getLocation());

        //FIXME Set job time
        holder.jobTime.setText("Time");
    }

    @Override
    public int getItemCount(){
        return jobsList.size();
    }
}
