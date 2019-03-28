package com.example.biddlr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import classes.DatabaseManager;
import classes.Job;


public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobViewHolder> {
    private List<Job> jobsList;
    private List<Bitmap> picsList;

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

    public JobListAdapter(List<Job> jobsList, List<Bitmap> picsList){
        this.jobsList = jobsList;
        this.picsList = picsList;
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
        //DatabaseManager.shared.setImage(job.getJobID(), holder.jobPicture);
        if(picsList != null && picsList.get(pos) != null) holder.jobPicture.setImageBitmap(picsList.get(pos));
        else holder.jobPicture.setImageResource(R.drawable.ic_biddlrlogo);

        //Set job title
        holder.jobTitle.setText(job.getTitle());

        //Set job price
        //TODO Set a formatter on the price input
        String price = "$" + String.format("%.2f", job.getStartingPrice());
        holder.jobPrice.setText(price);

        //Set job location
        if(job.getLocation() != null)
            holder.jobLocation.setText(job.getLocation());

        String time = timeLeft(job.getExpirationDate().toLocalDateTime());
        holder.jobTime.setText(time);
    }

    @Override
    public int getItemCount(){
        return jobsList.size();
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
