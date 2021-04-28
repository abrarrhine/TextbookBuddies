package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.textbookbuddies.models.User;
import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.search.Search;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;

public class Signup extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private static final String TAG = "Signup";
    Book book;
    String email;
    String password;
    String firstName;
    String lastName;
    String dob;
    String phoneNumber;

    Button signup;
    ArrayList<Book> booklist;

    EditText et_email;
    EditText et_password;
    EditText et_firstName;
    EditText et_lastName;
    EditText et_dob;
    EditText et_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        et_email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        et_password = (EditText) findViewById(R.id.editTextTextPassword);
        et_firstName = (EditText) findViewById(R.id.enterFirstNameTV);
        et_lastName = (EditText) findViewById(R.id.enterLastNameTV);
        et_dob = (EditText) findViewById(R.id.editTextDOB);
        et_number = (EditText) findViewById(R.id.editTextNumber);

        signup = (Button) findViewById(R.id.submitButton);
        //signup.setOnClickListener(this);
        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                signupUser();
            }
        });
        /**
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_username.getText().toString();
                password = et_password.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Signup.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        }); **/


    }

    private void updateUI(Object o) {
        if(o == mAuth.getCurrentUser()){
            Intent i = new Intent(Signup.this, Search.class);
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
        /**
        switch (v.getId()){
            case R.id.submitButton: signupUser();
            break;
        }
       **/
    }

    private void signupUser() {
        email= et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        firstName = et_firstName.getText().toString().trim();
        lastName = et_lastName.getText().toString().trim();
        dob = et_dob.getText().toString().trim();
        phoneNumber = et_number.getText().toString().trim();
        booklist = new ArrayList<Book>();
        if (email.isEmpty()){
            et_email.setError("Email is required");
            et_email.requestFocus();
            return;
        }
        if (password.isEmpty()){
            et_password.setError("Password is required");
            et_password.requestFocus();
            return;
        }
        if (firstName.isEmpty()){
            et_firstName.setError("First name is required");
            et_firstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()){
            et_lastName.setError("Last name is required");
            et_lastName.requestFocus();
            return;
        }
        if (dob.isEmpty()){
            et_dob.setError("Date of Birth is required");
            et_dob.requestFocus();
            return;
        }
        if (phoneNumber.isEmpty()){
            et_number.setError("Phone number is required");
            et_number.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Please provide your valid Virginia Tech email");
            et_email.requestFocus();
            return;
        }

        if (password.length() < 6 || password.length()> 12){
            et_password.setError("Password should be between 6-12 characters");
            et_password.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            book = new Book("title","isbn","author","classes", 50.80, phoneNumber,email);
                            booklist.add(book);
                            User user = new User(firstName, lastName, dob, email, phoneNumber, booklist);


                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Signup.this, "Sign up Successfull!", Toast.LENGTH_LONG).show();

                                        //redirect to login layout
                                        Intent login = new Intent(Signup.this, LoginActivity.class);
                                        startActivity(login);

                                    }
                                    else {
                                        Toast.makeText(Signup.this, "Sign up failed! Try again!", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(Signup.this, "Sign up failed! Try again!", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}