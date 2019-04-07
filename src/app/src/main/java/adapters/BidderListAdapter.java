package adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

import classes.DatabaseManager;
import classes.User;

/**
 * An adapter for a list of bidders for a job
 */
public class BidderListAdapter extends RecyclerView.Adapter<BidderListAdapter.BidderViewHolder> {
    private String jobId;
    private List<User> bids;
    private List<Bitmap> pics;
    private int selectedItem;

    /**
     * Describes the ui of each list item
     */
    class BidderViewHolder extends RecyclerView.ViewHolder{
        private CardView background;
        private ImageView imgUserPic;
        private TextView txtUserName;
        private TextView txtUserBid;

        private BidderViewHolder(View v) {
            super(v);
            background = v.findViewById(R.id.cardUser);
            imgUserPic = v.findViewById(R.id.imgUserPic);
            txtUserName = v.findViewById(R.id.txtUserName);
            txtUserBid = v.findViewById(R.id.txtUserBid);
        }
    }

    public BidderListAdapter(String jobId, List<User> bids, List<Bitmap> pics){
        this.jobId = jobId;
        this.bids = bids;
        this.pics = pics;
        selectedItem = -1;
    }

    /**
     * Creates the BidderViewHolder for each list item
     * @param v
     * @param viewType
     * @return The BidderViewHolder a list item
     */
    @NonNull
    @Override
    public BidderViewHolder onCreateViewHolder(@NonNull ViewGroup v, int viewType){
        View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.user_list_item, v, false);
        return new BidderListAdapter.BidderViewHolder(itemView);
    }

    /**
     * Sets values for each list item
     * @param holder Holds the ui elements for each list item
     * @param pos The index in the list for each list item
     */
    @Override
    public void onBindViewHolder(@NonNull BidderViewHolder holder, int pos){
        if(pos == selectedItem){
            holder.background.setCardBackgroundColor(Color.argb(128, 128, 128, 128));
        }
        else{
            holder.background.setCardBackgroundColor(Color.WHITE);
        }

        User user = bids.get(pos);

//        holder.imgUserPic.setImageResource(R.drawable.baseline_person_24);
        if(pics != null && pos < pics.size() && pics.get(pos) != null) holder.imgUserPic.setImageBitmap(pics.get(pos));
        else holder.imgUserPic.setImageResource(R.drawable.baseline_person_24);


        String fullName = user.getFirstName() + " " + user.getLastName();
        holder.txtUserName.setText(fullName);

        String value = String.format(Locale.ENGLISH, "$%.02f", user.getBiddedJobs().get(jobId));
        holder.txtUserBid.setText(value);
    }

    /**
     * Gets count of list items
     * @return Count of list items
     */
    @Override
    public int getItemCount(){ return bids.size(); }

    /**
     * Sets the currently selected list item
     * @param pos Index of the currently selected list item
     */
    public void setSelectedItem(int pos) {
        selectedItem = pos;
    }
}
