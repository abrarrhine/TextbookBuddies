package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.textbookbuddies.models.User;
import com.example.textbookbuddies.search.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

//    private FirebaseUser user;
    private Button editProfileButton;
    private DatabaseReference reference;
//    private String userID;
    TextView userEmail;
    TextView userFullName;
    TextView userPhoneNumber;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
//        userID = firebaseUser.getUid();
//        final TextView userFullName = (TextView) findViewById(R.id.fullNameProfile);
        final TextView userEmail = (TextView) findViewById(R.id.emailEditTextProfile);
        final TextView userPhoneNumber = (TextView) findViewById(R.id.phoneNumberTextProfile);

        userEmail.setText(firebaseUser.getEmail());
        userPhoneNumber.setText(firebaseUser.getPhoneNumber());

        editProfileButton = (Button) findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfileIntent = new Intent(Profile.this, EditProfile.class);
                startActivity(editProfileIntent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.ic_search:
                        Intent intent1 = new Intent(Profile.this, Search.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent(Profile.this, HomeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_listings:
                        Intent intent3 = new Intent(Profile.this, Listings.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_help:
                        Intent intent4 = new Intent(Profile.this, FAQ.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }
}