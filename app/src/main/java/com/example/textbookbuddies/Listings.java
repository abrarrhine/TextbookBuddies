package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicMarkableReference;

import okhttp3.Headers;

public class Listings extends AppCompatActivity {

    public static final String USER_INFO_URL = "https://textbook-buddies-31189-default-rtdb.firebaseio.com/users";

    RecyclerView bkListings;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    ArrayList<Book> userBookListings;
//    DatabaseReference listRef;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        TAG = "Listings";
        bkListings = findViewById(R.id.bkListings);
        bkListings.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String Uid = firebaseUser.getUid();
        String HttpURL = USER_INFO_URL + "/" + Uid + ".json";
// Create a storage reference from our app
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference bookListRef = storageRef.child(Uid + "/booklist");
        Log.d(TAG, HttpURL);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(HttpURL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray booklist = jsonObject.getJSONArray("booklist");
                            Log.i(TAG, "Results: " + booklist.toString());
//                            movies.addAll(Movie.fromJSONArray(results));
//                            movieAdapter.notifyDataSetChanged();
//                            Log.i(TAG, "Books: " + movies.size());

                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });

//                JSONObject bookList = curl 'https://dinosaur-facts.firebaseio.com/dinosaurs.json?orderBy="height"&startAt=3&print=pretty';


//        userBookListings = booklist;

//        FirebaseDatabase.getInstance().getReference("users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .getValue(firebaseUser)
//        userBookListings = firebaseUser;
//        listRef = firebaseDatabase.getReference().child(firebaseUser.getUid());
//        Log.d(TAG, booklist.toString());

//        https://textbook-buddies-31189-default-rtdb.firebaseio.com/users/

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.ic_search:
                        Intent intent1 = new Intent(Listings.this, Search.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent(Listings.this, HomeActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_listings:
                        Intent intent3 = new Intent(Listings.this, Listings.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_help:
                        Intent intent4 = new Intent(Listings.this, FAQ.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }
}