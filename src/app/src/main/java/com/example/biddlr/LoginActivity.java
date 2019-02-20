package com.example.biddlr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

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

    private void actionLogin() {
        //UtilityInterfaceTools.hideSoftKeyboard(LoginActivity.this);
        EditText txtEmail = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);

        if (txtEmail.getText().toString().equals("jsmith@ua.edu") && txtPassword.getText().toString().equals("testtest123")) {
            if (true) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        }

            else {
                TextView errorMessage = findViewById(R.id.message_invalid_credentials);
                errorMessage.setVisibility(View.VISIBLE);
                txtEmail.setText("");
                txtPassword.setText("");
            }

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

