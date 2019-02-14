package com.example.biddlr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent nextScreen = new Intent(v.getContext(), HomeActivity.class);
                startActivityForResult(nextScreen, 0);
            }
        });



            Button btnCreateUser = findViewById(R.id.btnCreateUser);
            btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent nextScreen = new Intent(v.getContext(), CreateUserActivity.class);
                   startActivityForResult(nextScreen, 0);
             }
            });
    }
}
