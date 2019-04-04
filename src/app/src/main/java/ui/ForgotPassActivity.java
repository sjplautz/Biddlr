package ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import java.util.Objects;

import classes.DatabaseManager;


//Forget password activity class

public class ForgotPassActivity extends AppCompatActivity {


    private EditText inputEmail;
    protected Button btnReset ;
    private ProgressBar progressBar;
    private ImageView imglogo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset Password");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgetpass);

        progressBar = findViewById(R.id.progressBar);
        imglogo= findViewById(R.id.imglogo);
        inputEmail = findViewById(R.id.txtEnEmail);
        btnReset= findViewById(R.id.btnSubmit);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                //Validate the email field
                if (TextUtils.isEmpty(email)) {

                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_LONG).show();

                    return;
                }

                //Handle the visibility of the progressBar,btnReset,inputEmail and imglogo
                progressBar.setVisibility(View.VISIBLE);
                btnReset.setVisibility(View.INVISIBLE);
                inputEmail.setVisibility(View.INVISIBLE);
                imglogo.setVisibility(View.VISIBLE);

                DatabaseManager.shared.mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Action process for reset password if email is valid
                                if (task.isSuccessful()) {

                                    Toast.makeText(ForgotPassActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ForgotPassActivity.this,LoginActivity.class));
                                }

                                // Display an error msg if email is not valid
                                else {
                                    Toast.makeText(ForgotPassActivity.this, "Failed to send reset email!", Toast.LENGTH_LONG).show();

                                }
                                //Handle the visibility of the progressBar,btnReset,inputEmail and imglogo
                                progressBar.setVisibility(View.GONE);
                                imglogo.setVisibility(View.INVISIBLE);
                                btnReset.setVisibility(View.VISIBLE);
                                inputEmail.setVisibility(View.VISIBLE);

                            }
                });
            }
        });
    }

}