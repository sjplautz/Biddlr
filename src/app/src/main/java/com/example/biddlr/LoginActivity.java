package com.example.biddlr;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import classes.DatabaseManager;

import static android.widget.Toast.LENGTH_LONG;


public class LoginActivity extends AppCompatActivity {


    //    private FirebaseAuth mAuth;
    private String TAG = "Login";
    private ProgressBar progressBar;
    private ImageView imglogo;
    private Button btnLogin;
    private Button btnCreateUser;
    private Button btnForgetPass;
    private TextView txtEmail;
    private TextView txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionLogin();
            }
        });

        Button btnCreateUser = findViewById(R.id.btnCreateUser);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCreateUser();
            }
        });

        Button btnForgetPass = findViewById(R.id.btnForgetPass);
        btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionForgetPass();
            }
        });
    }

    // Firebase provided method
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        DatabaseManager.shared.setUp();
        FirebaseUser currentUser = DatabaseManager.shared.mAuth.getCurrentUser();

        // update UI to start app with current user
        if (currentUser != null) {

            DatabaseManager.shared.setJobListener();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void actionLogin() {
        //UtilityInterfaceTools.hideSoftKeyboard(LoginActivity.this);
        final EditText txtEmail = findViewById(R.id.txtUsername);
         final EditText txtPassword = findViewById(R.id.txtPassword);
        imglogo= findViewById(R.id.imglogo);
        progressBar= findViewById(R.id.progressBar);
        btnLogin= findViewById(R.id.btnLogin);
        btnForgetPass= findViewById(R.id.btnForgetPass);
        btnCreateUser= findViewById(R.id.btnCreateUser);





        String email = txtEmail.getText().toString().trim();
         String password = txtPassword.getText().toString().trim();

        if ((TextUtils.isEmpty(email)) && (TextUtils.isEmpty(password))) {
            Toast.makeText(getApplication(), "Enter your email id and password", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "Enter your email id", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplication(), "Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        txtEmail.setVisibility(View.INVISIBLE);
        txtPassword.setVisibility(View.INVISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);
        btnForgetPass.setVisibility(View.INVISIBLE);
        btnCreateUser.setVisibility(View.INVISIBLE);
        imglogo.setVisibility(View.VISIBLE);

            DatabaseManager.shared.mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                DatabaseManager.shared.setUp();
                                FirebaseUser user = DatabaseManager.shared.mAuth.getCurrentUser();


                                if (user.isEmailVerified()) {
                                    Toast.makeText(LoginActivity.this, "Verified Email", Toast.LENGTH_SHORT).show();

                                    DatabaseManager.shared.setJobListener();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();

                                } else {
                                    Toast.makeText(LoginActivity.this, "Please Verified Email First", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    txtEmail.setVisibility(View.VISIBLE);
                                    txtPassword.setVisibility(View.VISIBLE);
                                    btnLogin.setVisibility(View.VISIBLE);
                                    btnForgetPass.setVisibility(View.VISIBLE);
                                    btnCreateUser.setVisibility(View.VISIBLE);
                                    imglogo.setVisibility(View.INVISIBLE);

                                }


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                txtEmail.setVisibility(View.VISIBLE);
                                txtPassword.setVisibility(View.VISIBLE);
                                btnLogin.setVisibility(View.VISIBLE);
                                btnForgetPass.setVisibility(View.VISIBLE);
                                btnCreateUser.setVisibility(View.VISIBLE);
                                imglogo.setVisibility(View.INVISIBLE);
                                txtPassword.setText("");
                            }
                        }
                    });


//        if (txtEmail.getText().toString().equals("jsmith@ua.edu") && txtPassword.getText().toString().equals("testtest123")) {
//            if (true) {
//                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                finish();
//            }
//        } else {
//                TextView errorMessage = findViewById(R.id.message_invalid_credentials);
//                errorMessage.setVisibility(View.VISIBLE);
//                txtEmail.setText("");
//                txtPassword.setText("");
//            }

        }



    private void actionCreateUser() {
        //  UtilityInterfaceTools.hideSoftKeyboard(LoginActivity.this);
        startActivity(new Intent(LoginActivity.this, CreateUserActivity.class));
        // finish();
    }

    private void actionForgetPass() {
        //  UtilityInterfaceTools.hideSoftKeyboard(LoginActivity.this);
        startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
        // finish();
    }
}


