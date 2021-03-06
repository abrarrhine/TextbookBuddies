package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.textbookbuddies.adapters.BookAdapter;
import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.search.Search;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Headers;

public class Listings extends AppCompatActivity {

    public static final String USER_INFO_URL = "https://textbook-buddies-31189-default-rtdb.firebaseio.com/users";

    RecyclerView bkListings;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    List<Book> books;
    String TAG;
    BookAdapter bookAdapter;
    FloatingActionButton addBookButt;
    String HttpURL;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        TAG = "Listings";
        addBookButt = findViewById(R.id.btAdd);
        bkListings = findViewById(R.id.bkListings);
        books = new ArrayList<>();
        bookAdapter = new BookAdapter(this, books);
        //set adapter on recycler view
        bkListings.setAdapter(bookAdapter);
        //set a layout manager on recycler view
        bkListings.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String Uid = firebaseUser.getUid();

        HttpURL = USER_INFO_URL + "/" + Uid + ".json";
        Log.d(TAG, HttpURL);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(HttpURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONObject booklist = jsonObject.getJSONObject("booklist");
                    Log.i(TAG, "Results: " + booklist.toString());

                    Iterator book = booklist.keys();
                    JSONArray newBookList = new JSONArray();
                    while (book.hasNext()){
                        String key = (String) book.next();
                        newBookList.put(booklist.get(key));
                    }
                    books.addAll(Book.fromJSONArray(newBookList));
                    bookAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Books: " + books.size());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
        addBookButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(Listings.this, AddListing.class);
                startActivity(addIntent);
            }
        });

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
                        Intent intent2 = new Intent(Listings.this, Profile.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // do something here
            Intent intent5 = new Intent(Listings.this, LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent5);
        }

        if (id == R.id.refresh) {
            // do something here
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh(){
        Intent thisIntent = new Intent(Listings.this, Listings.class);
        startActivity(thisIntent);
    }
}