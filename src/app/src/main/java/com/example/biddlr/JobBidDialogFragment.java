package com.example.biddlr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import classes.DatabaseManager;
import classes.Job;

public class JobBidDialogFragment extends DialogFragment {
    private static final String JOB_BID_DIALOG_KEY = "job_bid_dialog_key";
    private Job job;
    private EditText txtBid;

    public static JobBidDialogFragment newInstance(Job job) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(JOB_BID_DIALOG_KEY, job);
        JobBidDialogFragment fragment = new JobBidDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        DatabaseManager.shared.getUserFromID(job.getPosterID(), this);
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_fragment_job_bid, null);

        job = getArguments().getParcelable(JOB_BID_DIALOG_KEY);

        TextView txtJobAskingPrice = (TextView) v.findViewById(R.id.txtJobAskingPrice);
        String start = "$" + job.getStartingPrice();
        txtJobAskingPrice.setText(start);

        TextView txtJobCurrentBid = (TextView) v.findViewById(R.id.txtJobCurrentBid);
        String current = "$" + job.getCurrentBid();
        txtJobCurrentBid.setText(current);

        txtBid = (EditText) v.findViewById(R.id.txtNewBid);
        Button btnPlaceBid = v.findViewById(R.id.btnPlaceBid);
        btnPlaceBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bid = txtBid.getText().toString();
                if (bid.trim().isEmpty()) { return; }
                job.addBid(DatabaseManager.shared.getCurrentUser().getUid(), new Double(bid));
                dismiss();
            }
        });

        Button btnCancel = v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(v);
        return builder.create();

    }
}