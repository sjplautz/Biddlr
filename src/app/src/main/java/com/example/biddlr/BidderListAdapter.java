package com.example.biddlr;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import classes.User;

public class BidderListAdapter extends RecyclerView.Adapter<BidderListAdapter.BidderViewHolder> {
    private List<Pair<String, Double>> bids;
    private List<Bitmap> pics;

    class BidderViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgUserPic;
        private TextView txtUserName;
        private TextView txtUserBid;

        private BidderViewHolder(View v) {
            super(v);
            imgUserPic = v.findViewById(R.id.imgUserPic);
            txtUserName = v.findViewById(R.id.txtUserName);
            txtUserBid = v.findViewById(R.id.txtUserBid);
        }
    }

    public BidderListAdapter(List<Pair<String, Double>> bids, List<Bitmap> pics){
        this.bids = bids;
        this.pics = pics;
    }

    @NonNull
    @Override
    public BidderViewHolder onCreateViewHolder(@NonNull ViewGroup v, int viewType){
        View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.user_list_item, v, false);
        return new BidderListAdapter.BidderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BidderViewHolder holder, int pos){
        Pair<String, Double> bid = bids.get(pos);

        holder.imgUserPic.setImageResource(R.drawable.ic_biddlrlogo);

        String fullName = bid.first;
        holder.txtUserName.setText(fullName);

        String value = "$" + bid.second.toString();
        holder.txtUserBid.setText(value);
    }

    @Override
    public int getItemCount(){ return bids.size(); }
}
