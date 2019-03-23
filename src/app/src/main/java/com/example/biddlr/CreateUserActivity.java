package com.example.biddlr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import classes.DatabaseManager;


public class CreateUserActivity extends AppCompatActivity {

    String TAG = "Create User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);
        getSupportActionBar().hide();
        Button buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionCreateUser();
            }
        });
    }

    private void actionCreateUser() {
        UtilityInterfaceTools.hideSoftKeyboard(CreateUserActivity.this);

//        AlertDialog.Builder b = new AlertDialog.Builder(CreateUserActivity.this);
//        b.setCancelable(true);
//        b.setTitle("Your Account Has Been Created");
//        b.setMessage("Use your email and password to login.");
//        b.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
//                        finish();
//                    }
//                });

        // Need some verification here
        EditText txtEmail = findViewById(R.id.txtEnEmail);
        EditText txtPassword = findViewById(R.id.txtEnPass);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        DatabaseManager.shared.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = DatabaseManager.shared.getCurrentUser();
                            startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

//        AlertDialog confirmationDialog = b.create();
//        confirmationDialog.show();
    }
}
