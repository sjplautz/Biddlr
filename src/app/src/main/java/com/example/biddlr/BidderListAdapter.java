package com.example.biddlr;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import classes.User;

public class BidderListAdapter extends RecyclerView.Adapter<BidderListAdapter.BidderViewHolder> {
    private String jobId;
    private List<User> bids;
    private List<Bitmap> pics;
    private int selectedItem;

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

    @NonNull
    @Override
    public BidderViewHolder onCreateViewHolder(@NonNull ViewGroup v, int viewType){
        View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.user_list_item, v, false);
        return new BidderListAdapter.BidderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BidderViewHolder holder, int pos){
        if(pos == selectedItem){
            holder.background.setCardBackgroundColor(Color.argb(128, 128, 128, 128));
        }
        else{
            holder.background.setCardBackgroundColor(Color.WHITE);
        }

        User user = bids.get(pos);

        holder.imgUserPic.setImageResource(R.drawable.ic_biddlrlogo);

        String fullName = user.getFirstName() + " " + user.getLastName();
        holder.txtUserName.setText(fullName);

        String value = "$" + user.getBiddedJobs().get(jobId);
        holder.txtUserBid.setText(value);
    }

    @Override
    public int getItemCount(){ return bids.size(); }

    public void setSelectedItem(int pos) {
        selectedItem = pos;
    }
}
