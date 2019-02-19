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

public class ForgetPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
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
        AlertDialog.Builder b = new AlertDialog.Builder(ForgetPassActivity.this);
        b.setCancelable(true);
        b.setTitle("Check Your Inbox");
        b.setMessage("A reset password link has been sent to " + sEmail + ".");
        b.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(ForgetPassActivity.this, LoginActivity.class));
                        finish();
                    }
                });

        AlertDialog confirmationDialog = b.create();
        confirmationDialog.show();
    }
}