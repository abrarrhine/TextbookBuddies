package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import okhttp3.Headers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.textbookbuddies.adapters.BookAdapter;
import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.models.User;
import com.example.textbookbuddies.search.Search;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    ImageView iv_btn_back;
    TextView tv_password_link;
    TextView tv_username_top;
    TextView tv_username_mid;
    TextView tv_logout;

    //private FirebaseDatabase firebaseDatabase;
    //private DatabaseReference databaseReference;
    private String TAG = "EditProfile";
    private List<User> users;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    public static final String USER_INFO_URL = "https://textbook-buddies-31189-default-rtdb.firebaseio.com/users";

    private Toolbar mToolbar;
    LinearLayout ll_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        tv_username_top = (TextView) findViewById(R.id.tv_username_top);
        tv_username_mid = (TextView) findViewById(R.id.tv_username_mid);

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
                    Log.i(TAG, "Results: " + firstName);
                    tv_username_top.setText(firstName + " " + lastName + "!");
                    tv_username_mid.setText(firstName + " " + lastName);

                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        tv_password_link = (TextView)findViewById(R.id.tv_password_link);

        tv_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, ChangePassword.class);
                startActivity(intent);
            }
        });

        iv_btn_back = (ImageView) findViewById(R.id.iv_btn_back);

        iv_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });

        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.ic_search:
                        Intent intent1 = new Intent(EditProfile.this, Search.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent(EditProfile.this, Profile.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_listings:
                        Intent intent3 = new Intent(EditProfile.this, Listings.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_help:
                        Intent intent4 = new Intent(EditProfile.this, FAQ.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

    /*@Override
    public void onStart() {
        super.onStart();

        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        Log.i(TAG, "Value = " + snapshot.child("users").getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditProfile.this, error + TAG, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }*/

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
            Intent intent5 = new Intent(EditProfile.this, LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent5);
        }
        return super.onOptionsItemSelected(item);
    }
}