package ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import classes.DatabaseManager;

//Login activity class
public class LoginActivity extends AppCompatActivity {

    private String TAG = "Login";

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

    // Firebase provided method
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and upda   te UI accordingly.
        DatabaseManager.shared.setUp();
        FirebaseUser currentUser = DatabaseManager.shared.getFirebaseUser();

        // update UI to start app with current user
        if (currentUser != null) {
            DatabaseManager.shared.setCurrentUser();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }
// Handle's the login process
    private void actionLogin() {

        EditText txtEmail = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        //Validate user's email and password fields
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


        DatabaseManager.shared.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = DatabaseManager.shared.getFirebaseUser();
                            // Check if the user email is verified before the first login
                            if (user.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Verified Email", Toast.LENGTH_SHORT).show();

                                DatabaseManager.shared.setCurrentUser();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    // Launch create user activity
    private void actionCreateUser() {

        startActivity(new Intent(LoginActivity.this, CreateUserActivity.class));

    }
    // Launch reset user password activity
    private void actionForgetPass() {

        startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));

    }
}

