package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Headers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.textbookbuddies.models.User;
import com.example.textbookbuddies.search.Search;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Profile extends AppCompatActivity {

    private static final String TAG = "Profile";
    //    private FirebaseUser user;
    private Button editProfileButton;
    private DatabaseReference reference;
//    private String userID;
    TextView userEmail;
    TextView userFullName;
    TextView userPhoneNumber;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    public static final String USER_INFO_URL = "https://textbook-buddies-31189-default-rtdb.firebaseio.com/users";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final TextView userEmail = (TextView) findViewById(R.id.emailEditTextProfile);
        userPhoneNumber = (TextView) findViewById(R.id.phoneNumberTextProfile);

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

        userFullName = (TextView) findViewById(R.id.fullNameProfile);
        userPhoneNumber = (TextView) findViewById(R.id.phoneNumberTextProfile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String Uid = firebaseUser.getUid();

        String HttpURL = USER_INFO_URL + "/" + Uid + ".json";
        Log.d(TAG, HttpURL);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(HttpURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    String firstName = jsonObject.getString("firstname");
                    String lastName = jsonObject.getString("lastname");
                    String phoneNumber = jsonObject.getString("phonenumber");
                    Log.i(TAG, "Results: " + firstName);
                    userFullName.setText(firstName + " " + lastName);
                    userFullName.setText(firstName + " " + lastName);
                    userPhoneNumber.setText(phoneNumber);

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
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
                        Intent intent2 = new Intent(Profile.this, Profile.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // do something here
            Intent intent5 = new Intent(Profile.this, LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent5);
        }
        return super.onOptionsItemSelected(item);
    }
}