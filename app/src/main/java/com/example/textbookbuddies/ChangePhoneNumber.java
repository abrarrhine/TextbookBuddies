package com.example.textbookbuddies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePhoneNumber extends AppCompatActivity {

    private EditText editPhoneNumber;
    private Button btn_change_password;
    private ProgressBar progressBar;
    FirebaseAuth auth;

    String newNumber;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference firebaseDatabase;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);

        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        btn_change_password = (Button) findViewById(R.id.btn_change_password);

        btn_change_password.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changePhoneNumber();
                Toast.makeText(ChangePhoneNumber.this, "Phone number updated!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void changePhoneNumber(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        String Uid = firebaseUser.getUid();
        newNumber = editPhoneNumber.getText().toString();
        firebaseDatabase.child("users").child(Uid).child("phonenumber").setValue(newNumber);
        Intent intent = new Intent();
        intent.putExtra("newNumber", newNumber);
        setResult(RESULT_OK, intent);


    }
}