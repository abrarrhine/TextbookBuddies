package com.example.textbookbuddies.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.textbookbuddies.ForgotPassword;
import com.example.textbookbuddies.R;
import com.example.textbookbuddies.Signup;
import com.example.textbookbuddies.search.Search;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    String email;
    String password;

    Button login;
    Button signup;
    Button forgotPassword;
    ProgressBar progressBar;

    private EditText et_usernamelogin;
    private EditText et_passwordlogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        et_usernamelogin = (EditText) findViewById(R.id.et_username);
        et_passwordlogin = (EditText) findViewById(R.id.et_password);

        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        forgotPassword = (Button) findViewById(R.id.forgotPasswordButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                userlogin();
            } });
            /**
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_usernamelogin.getText().toString();
                password = et_passwordlogin.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });
         **/
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(LoginActivity.this, Signup.class);
                startActivity(signupIntent);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent forgotpIntent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(forgotpIntent);
            }
        });
    }

    private void userlogin(){
        email = et_usernamelogin.getText().toString().trim();
        password = et_passwordlogin.getText().toString().trim();

        if (email.isEmpty()){
            et_usernamelogin.setError("Email is required!");
            et_usernamelogin.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_usernamelogin.setError("Please provide a valid email!");
            et_usernamelogin.requestFocus();
            return;
        }

        if (password.isEmpty()){
            et_passwordlogin.setError("Password is required!");
            et_passwordlogin.requestFocus();
            return;
        }

        if (password.length() < 6 || password.length() > 12){
            et_passwordlogin.setError("Password should be between 6-12 characters!");
            et_passwordlogin.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.VISIBLE);
                if (task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                    if (user.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_LONG).show();

                        //redirect to home page
                        Intent i = new Intent(LoginActivity.this, Search.class);
                        startActivity(i);
                    }
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);

                }
                else{
                    Toast.makeText(LoginActivity.this, "Incorrect Email or Password!     Log in failed! Try again!", Toast.LENGTH_LONG).show();
                    //Toast.makeText(LoginActivity.this, "Log in failed! Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void updateUI(Object o) {
        if(o == mAuth.getCurrentUser()){
            Intent i = new Intent(LoginActivity.this, Search.class);
            i.putExtra("userobject", Parcels.wrap(o));
            startActivity(i);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    @Override
    public void onClick(View v) {

    }
}