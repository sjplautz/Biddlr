package ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

//    private FirebaseAuth mAuth;
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
        // Check if user is signed in (non-null) and update UI accordingly.
        DatabaseManager.shared.setUp();
        FirebaseUser currentUser = DatabaseManager.shared.getFirebaseUser();

        // update UI to start app with current user
        if (currentUser != null) {
            DatabaseManager.shared.setCurrentUser();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void actionLogin() {
        //UtilityInterfaceTools.hideSoftKeyboard(LoginActivity.this);
        EditText txtEmail = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        DatabaseManager.shared.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = DatabaseManager.shared.getFirebaseUser();

                            if (user.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Verified Email", Toast.LENGTH_SHORT).show();

                                DatabaseManager.shared.setCurrentUser();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();

                            }
                                //startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                          //  finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
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

