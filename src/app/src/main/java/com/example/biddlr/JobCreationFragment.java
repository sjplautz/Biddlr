package com.example.biddlr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static android.app.Activity.RESULT_OK;

//TODO Move submission code here
public class JobCreationFragment extends DialogFragment {
    private ImageButton btnImagePicker;
    private BottomSheetDialog typePicker;

    public static JobCreationFragment newInstance() {
        JobCreationFragment fragment = new JobCreationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_job_creation, container, false);

        LocalDateTime now = LocalDateTime.now();

        String date = now.format(DateTimeFormatter.ofPattern("MM/dd/uuuu"));
        TextView txtDate = v.findViewById(R.id.txtDate);
        txtDate.setText(date);

        String time = now.format(DateTimeFormatter.ofPattern("hh:mm a"));
        TextView txtTime = v.findViewById(R.id.txtTime);
        txtTime.setText(time);

        ImageButton btnDate = v.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker dp = new DatePicker();
                dp.show(getChildFragmentManager(), null);
            }
        });

        ImageButton btnTime = v.findViewById(R.id.btnTime);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker tp = new TimePicker();
                tp.show(getChildFragmentManager(), null);
            }
        });

        btnImagePicker = v.findViewById(R.id.btnImagePicker);
        btnImagePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePicker = new BottomSheetDialog(getActivity());
                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.dialog_image_type, null);

                ImageButton btnCamera = sheetView.findViewById(R.id.btnCamera);
                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraPicker = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraPicker, 0);
                    }
                });

                ImageButton btnGallery = sheetView.findViewById(R.id.btnGallery);
                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent galleryPicker = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryPicker, 1);
                    }
                });

                typePicker.setContentView(sheetView);
                typePicker.show();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bitmap camImage = (Bitmap)data.getExtras().get("data");
                    btnImagePicker.setImageBitmap(camImage);
                    break;
                case 1:
                    Bitmap image = null;
                    try {
                        image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnImagePicker.setImageBitmap(image);
                    break;
            }

            typePicker.hide();
        }
        else{
            System.out.println(resultCode);
            System.out.println("ERROR");
        }
    }
}
