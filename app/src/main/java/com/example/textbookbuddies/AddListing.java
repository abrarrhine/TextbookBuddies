package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.textbookbuddies.R;
import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.models.User;
import com.example.textbookbuddies.search.*;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import okhttp3.Headers;

public class AddListing extends AppCompatActivity {

    private static final String TAG = "AddListing";
    public static final String USER_INFO_URL = "https://textbook-buddies-31189-default-rtdb.firebaseio.com/users";

    TextView title, author,isbn, price, email, phonenumber, classes;
    Button cancel, submit;
    List<Book> oldbooklist;
    String userId;

    private DatabaseReference firebaseDatabase;
    private DatabaseReference listingsRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        listingsRef = FirebaseDatabase.getInstance().getReference().child("listings");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        String HttpURL = USER_INFO_URL + "/" + userId + ".json";
        oldbooklist = new ArrayList<>();
        title = findViewById(R.id.bkAddtitle);
        author = findViewById(R.id.bkAddauthor);
        classes = findViewById(R.id.bkAddclasses);
        isbn = findViewById(R.id.bkAddisbn);
        price = findViewById(R.id.bkAddprice);
        email = findViewById(R.id.bkAddemail);
        phonenumber = findViewById(R.id.bkAddphonenumber);
        cancel = findViewById(R.id.btcancel);
        submit = findViewById(R.id.btsubmit);

        email.setText(firebaseUser.getEmail());
        phonenumber.setText(firebaseUser.getPhoneNumber());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent(AddListing.this, Listings.class);
                startActivity(cancelIntent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book newbook = new Book(
                        title.getText().toString(),
                        isbn.getText().toString(),
                        author.getText().toString(),
                        classes.getText().toString(),
                        Double.parseDouble(price.getText().toString()),
                        phonenumber.getText().toString(),
                        email.getText().toString());

                AsyncHttpClient client = new AsyncHttpClient();
                client.get(HttpURL, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess");
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray booklist = jsonObject.getJSONArray("booklist");
                            Log.i(TAG, "Results: " + booklist.toString());
                            oldbooklist.addAll(Book.fromJSONArray(booklist));
                            Log.i(TAG, "Books: " + oldbooklist.size());
                            oldbooklist.add(newbook);
                            firebaseDatabase.child("users").child(userId).child("booklist").setValue(oldbooklist);
                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                            addBookList(newbook);
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
                // make listings section
                listingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key = listingsRef.push().getKey();
                        listingsRef.child(key).setValue(newbook);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //end
                Intent submitIntent = new Intent(AddListing.this, Listings.class);
                startActivity(submitIntent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.ic_search:
                        Intent intent1 = new Intent(AddListing.this, Search.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent(AddListing.this, Profile.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_listings:
                        Intent intent3 = new Intent(AddListing.this, Listings.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_help:
                        Intent intent4 = new Intent(AddListing.this, FAQ.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

    public void addBookList(Book newbook){
        firebaseDatabase.child("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    HashMap oldUser = (HashMap) task.getResult().getValue();
                    ArrayList<Book> newBookList = new ArrayList<Book>();
                    newBookList.add(newbook);
                    oldUser.put("booklist", newBookList);
                    firebaseDatabase.child("users").child(userId).setValue(oldUser);
                }
            }
        });
    }
}