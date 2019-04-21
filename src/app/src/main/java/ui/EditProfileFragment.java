package ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.biddlr.R;

import java.io.ByteArrayOutputStream;

import classes.DatabaseManager;
import classes.User;

import static android.app.Activity.RESULT_OK;


public class EditProfileFragment extends Fragment{
    private ImageView imgProfilePicture;
    private BottomSheetDialog typePicker;
    private EditText editProfile;

    private User currentUser;


    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = DatabaseManager.shared.currentUser;
    }

    @Override
    //specifies the instructions to be executed for the fragment once it has been allocated
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        //get handle to my profile bio
        editProfile = v.findViewById(R.id.txtEditBio);

        //set text to my bio
        String myBio = currentUser.getBio();
        editProfile.setText(myBio);

        imgProfilePicture = v.findViewById(R.id.imgMyProfileImage);
        DatabaseManager.shared.setUserImage(currentUser.getId(), imgProfilePicture);

        FloatingActionButton btnImagePicker = v.findViewById(R.id.btnProfileImagePicker);
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

        Button btnSaveChanges = v.findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser = DatabaseManager.shared.currentUser;
                currentUser.setBio(editProfile.getText().toString());
                byte[] imgArr = null;
                if(imgProfilePicture.getDrawable().equals(Drawable.createFromPath("/drawable/baseline_person_24.xml"))) {
                    Bitmap image = ((BitmapDrawable) imgProfilePicture.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imgArr = stream.toByteArray();
                    image.recycle();
                }
                DatabaseManager.shared.updateUser(currentUser, imgArr);
                getFragmentManager().popBackStack();
            }
        });

        return v;
    }

    /**
     * Handles the results of the Intents used to retrieve the job image
     * @param requestCode The requestCode of the Intent
     * @param resultCode The success state of the Intent
     * @param data The result of the Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    Bitmap camImage = (Bitmap)data.getExtras().get("data");
                    imgProfilePicture.setImageBitmap(camImage);
                    break;
                case 1:
                    imgProfilePicture.setImageURI(data.getData());
                    break;
            }
            imgProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
            typePicker.hide();
        }
        else{
            System.out.println(resultCode);
            System.out.println("ERROR");
        }
    }
}