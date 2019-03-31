package com.example.biddlr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import classes.DatabaseManager;

import java.util.Objects;

public class CreateUserActivity extends AppCompatActivity {

    EditText txtFname, txtLname,txtEnPass, txtReEnPass, txtEnEmail;
    boolean flag = false;
    Button btnSubmit;
    Boolean isRegister = false;
    private FirebaseAuth mAuth;
    private String TAG = getClass().getSimpleName();
    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);

        //mAuth = FirebaseAuth.getInstance();
        txtEnEmail = (EditText) findViewById(R.id.txtEnEmail);
        txtFname = (EditText) findViewById(R.id.txtFname);
        txtLname = (EditText) findViewById(R.id.txtLname);
        txtEnPass = (EditText) findViewById(R.id.txtEnPass);
        txtReEnPass = (EditText) findViewById(R.id.txtReEnPass);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        editor = getSharedPreferences("Main", MODE_PRIVATE).edit();
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Account");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Befor Registration Validate Fields
                if (validateFields()) {


                    registerUser();

                }
            }
        });



    }


    /*
      After validatation register the user to firebase
    */

    private boolean registerUser() {

        String email = txtEnEmail.getText().toString().trim();
        String password = txtEnPass.getText().toString().trim();

        DatabaseManager.shared.mAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {

       // DatabaseManager.shared.mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<Void>() {


                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.w(TAG, "createUserWithEmail:success");
                            Toast.makeText(CreateUserActivity.this, "Authentication Success.", Toast.LENGTH_SHORT).show();

                            sendRegistrationLink();
                        //    startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                            isRegister= true;


                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUserActivity.this, "User Already exist.", Toast.LENGTH_SHORT).show();
                            isRegister= false;
                        }

                    }
                });
        return isRegister;
    }

/*
      After Registration send the authentication link to mail using firebase auth
    */

    private void sendRegistrationLink() {
        //DatabaseManager.shared.setUp();
        final FirebaseUser user = DatabaseManager.shared.mAuth.getCurrentUser();
        assert user != null;
        final Task sendEmailVerification;
        sendEmailVerification = user.sendEmailVerification()
                .addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateUserActivity.this,
                                    "Verification email sent to " + user.getEmail(),

                                    Toast.LENGTH_SHORT).show();


                            startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                            finish();
                            //validateUsername();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(CreateUserActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /*
      I am saving username in-order to use it after login
    */

  //  private void validateUsername() {
    //    editor.putString("name", txtEnEmail.getText().toString().trim());
      //  editor.commit();
        //Intent i = new Intent(CreateUserActivity.this, LoginActivity.class);
        //startActivity(i);
       // finish();
    //}

    /*
      Vadidating all field
    */
    private boolean validateFields() {


        if (txtEnEmail.getText().toString().trim().equalsIgnoreCase("")
                || txtEnEmail.getText().toString().trim().equalsIgnoreCase(null)) {
            Toast.makeText(CreateUserActivity.this, "Plaese Enter your email", Toast.LENGTH_LONG).show();
            flag = false;
        } else if (txtFname.getText().toString().trim().equalsIgnoreCase("")
                || txtEnEmail.getText().toString().trim().equalsIgnoreCase(null)) {
            Toast.makeText(CreateUserActivity.this, "Plaese Enter your first name", Toast.LENGTH_LONG).show();
            flag = false;
        }else if (txtLname.getText().toString().trim().equalsIgnoreCase("")
                || txtEnEmail.getText().toString().trim().equalsIgnoreCase(null)) {
            Toast.makeText(CreateUserActivity.this, "Plaese Enter your last name", Toast.LENGTH_LONG).show();
            flag = false;
        }

        else if (txtEnPass.getText().toString().trim().equalsIgnoreCase("")
                || txtEnEmail.getText().toString().trim().equalsIgnoreCase(null)) {
            Toast.makeText(CreateUserActivity.this, "Password should not be blank", Toast.LENGTH_LONG).show();

            flag = false;
        } else if (!txtEnPass.getText().toString().equals(txtReEnPass.getText().toString())) {
            Toast.makeText(CreateUserActivity.this, "Password does not match ", Toast.LENGTH_LONG).show();
            flag = false;
        }else if (txtEnPass.getText().toString().length()<6) {
            Toast.makeText(CreateUserActivity.this, "Password must be 6 digit long ", Toast.LENGTH_LONG).show();
            flag = false;
        } else {
            flag = true;
        }

        return flag;

    }

}
