package adapters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.biddlr.R;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import classes.Job;

/**
 * An adapter for a list of completed jobs
 */
public class CompletedJobAdapter extends RecyclerView.Adapter<CompletedJobAdapter.JobViewHolder> {
    private List<Job> jobsList;
    private List<Bitmap> picsList;

    private String filter;

    /**
     * Describes the ui of each list item
     */
    class JobViewHolder extends RecyclerView.ViewHolder{
        private ImageView jobPicture;
        private TextView jobTitle;
        private TextView jobPrice;
        private TextView jobLocation;
        private TextView jobTime;
        private RatingBar jobRating;

        private JobViewHolder(View v) {
            super(v);
            jobPicture = v.findViewById(R.id.completed_imgJobPic);
            jobTitle = v.findViewById(R.id.completed_txtJobTitle);
            jobPrice = v.findViewById(R.id.completed_txtStartingPrice);
            jobLocation = v.findViewById(R.id.completed_txtJobLocation);
            jobTime = v.findViewById(R.id.completed_txtTimeRemaining);
            jobRating = v.findViewById(R.id.completed_job_Ratingbar);
        }
    }

    public CompletedJobAdapter(List<Job> jobsList, List<Bitmap> picsList){
        this.jobsList = jobsList;
        this.picsList = picsList;
    }

    /**
     * Creates the JobViewHolder for each list item
     * @param v
     * @param viewType
     * @return The JobViewHolder a list item
     */
    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup v, int viewType){
        View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.completed_job_list_item, v, false);
        return new JobViewHolder(itemView);
    }

    /**
     * Sets values for each list item
     * @param holder Holds the ui elements for each list item
     * @param pos The index in the list for each list item
     */
    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int pos){
        Job job = jobsList.get(pos);

        //Set up job picture
        if(picsList != null && pos < picsList.size() && picsList.get(pos) != null) holder.jobPicture.setImageBitmap(picsList.get(pos));
        else holder.jobPicture.setImageResource(R.drawable.ic_camera_default_gray);

        //Set job title
        holder.jobTitle.setText(job.getTitle());

        //Set job price
        //TODO Set a formatter on the price input
        String price = "$" + String.format("%.2f", job.getCurrentBid());
        holder.jobPrice.setText(price);

        //Set job location
        if(job.getLocation() != null)
            holder.jobLocation.setText(job.getLocation());

        holder.jobRating.setNumStars(5);
        holder.jobRating.setRating(job.getCurrentRating().floatValue());

        String time = timeLeft(job.getExpirationDate().toLocalDateTime());
        holder.jobTime.setText(time);
    }

    /**
     * Gets count of list items
     * @return Count of list items
     */
    @Override
    public int getItemCount(){
        if(filter == null || filter.isEmpty()) return jobsList.size();

        int count = 0;
        for(Job job: jobsList){
            if(job.getTitle().contains(filter))
                ++count;
        }
        return count;
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
