package com.example.biddlr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class CreateUserActivity extends AppCompatActivity {

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

        AlertDialog.Builder b = new AlertDialog.Builder(CreateUserActivity.this);
        b.setCancelable(true);
        b.setTitle("Your Account Has Been Created");
        b.setMessage("Use your email and password to login.");
        b.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                        finish();
                    }
                });

        AlertDialog confirmationDialog = b.create();
        confirmationDialog.show();
    }
}
