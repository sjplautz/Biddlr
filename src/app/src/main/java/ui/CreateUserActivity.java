package ui;

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

import com.example.biddlr.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import classes.DatabaseManager;
import classes.User;

//Create user activity class
public class CreateUserActivity extends AppCompatActivity {

    EditText txtFname, txtLname,txtEnPass, txtReEnPass, txtEnEmail;
    boolean flag = false;
    Button btnSubmit;
    Boolean isRegister = false;
    private String TAG = getClass().getSimpleName();
    SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);


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

                //Validate Fields Before Registration
                if (validateFields()) {

                    registerUser();
                }
            }
        });

    }


    //  After validation register the user to firebase
    private boolean registerUser() {


        EditText txtFname = findViewById(R.id.txtFname);
        EditText txtLname = findViewById(R.id.txtLname);
        EditText txtEmail = findViewById(R.id.txtEnEmail);
        EditText txtPassword = findViewById(R.id.txtEnPass);

        final String fname = txtFname.getText().toString();
        final String lname = txtLname.getText().toString();
        final String email = txtEmail.getText().toString();
        final String password = txtPassword.getText().toString();

        DatabaseManager.shared.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateUserActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            User user = new User(fname, lname, email);
                            DatabaseManager.shared.addNewUser(user);
                            sendRegistrationLink();
                            DatabaseManager.shared.mAuth.signOut();
                            startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                            finish();

                            isRegister= true;

                        } else {
                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateUserActivity.this, "User Already exist.", Toast.LENGTH_SHORT).show();
                            isRegister= false;
                        }

                    }
                });
        return isRegister;
    }


    //After Registration send the authentication link to mail using firebase auth

    private void sendRegistrationLink() {

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

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(CreateUserActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Handle fields validation
    private boolean validateFields() {


        if (txtEnEmail.getText().toString().trim().equalsIgnoreCase("") && txtFname.getText().toString().trim().equalsIgnoreCase("")
                && txtLname.getText().toString().trim().equalsIgnoreCase("") && txtEnPass.getText().toString().trim().equalsIgnoreCase("")
        && txtReEnPass.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(CreateUserActivity.this, "All fields are required please fill them all", Toast.LENGTH_LONG).show();
            flag = false;
        } else if (txtFname.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(CreateUserActivity.this, "Please Enter your first name", Toast.LENGTH_LONG).show();
            flag = false;
        }else if (txtLname.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(CreateUserActivity.this, "Please Enter your last name", Toast.LENGTH_LONG).show();
            flag = false;
        }
        else if (txtEnEmail.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(CreateUserActivity.this, "Please Enter your email", Toast.LENGTH_LONG).show();
            flag = false;
        }
        else if (txtEnPass.getText().toString().trim().equalsIgnoreCase("")) {
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
