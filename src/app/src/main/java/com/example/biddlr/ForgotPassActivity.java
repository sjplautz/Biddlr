package com.example.biddlr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        getSupportActionBar().hide();
        Button buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                actionForgetPass();
            }
        });
    }

    private void actionForgetPass(){
        EditText inputEmail = (EditText) findViewById(R.id.txtEnEmail);
        String sEmail = inputEmail.getText().toString();
        AlertDialog.Builder b = new AlertDialog.Builder(ForgotPassActivity.this);
        b.setCancelable(true);
        b.setTitle("Check Your Inbox");
        b.setMessage("A reset password link has been sent to " + sEmail + ".");
        b.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ForgotPassActivity.this, LoginActivity.class));
                        finish();
                    }
                });

        AlertDialog confirmationDialog = b.create();
        confirmationDialog.show();
    }
}