/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.textbookbuddies.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.textbookbuddies.BottomNavigationViewHelper;
import com.example.textbookbuddies.FAQ;
import com.example.textbookbuddies.HomeActivity;
import com.example.textbookbuddies.Listings;
import com.example.textbookbuddies.Location;
import com.example.textbookbuddies.Profile;
import com.example.textbookbuddies.R;
import com.example.textbookbuddies.adapters.BookAdapter;
import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Search extends AppCompatActivity implements
        View.OnClickListener,
        FilterDialogFragment.FilterListener {

    private static final String TAG = "Search";

    private static final int LIMIT = 50;

    private Toolbar mToolbar;
    private EditText mCurrentSearch;
    private TextView mCurrentSortByView;
    private RecyclerView mBooksRecycler;
    private ViewGroup mEmptyView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Query mQuery;

    private FilterDialogFragment mFilterDialog;
    private BookAdapter bookAdapter;
    private List<Book> books;

    private SearchViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mCurrentSearch = (EditText) findViewById(R.id.text_current_search);
        mBooksRecycler = findViewById(R.id.recycler_books);
        mEmptyView = findViewById(R.id.view_empty);

        findViewById(R.id.filter_bar).setOnClickListener(this);
        findViewById(R.id.button_clear_filter).setOnClickListener(this);

        // View model
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("listings");

        mQuery = databaseReference.orderByChild("title");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    books.add(snapshot.getValue(Book.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    books.remove(snapshot.getValue(Book.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        books = new ArrayList<>();
        mBooksRecycler = findViewById(R.id.recycler_books);
        mBooksRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Filter Dialog
        mFilterDialog = new FilterDialogFragment();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_search:
                        Intent intent1 = new Intent(Search.this, Search.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent(Search.this, Profile.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_listings:
                        Intent intent3 = new Intent(Search.this, Listings.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_help:
                        Intent intent4 = new Intent(Search.this, FAQ.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

    private Book convertMapToBook(Object obj) {
        if (obj instanceof Map) {
            Map map = (HashMap<String, Object>) obj;
            return new Book((String) map.get("title"), (String) map.get("isbn"), (String) map.get("author"), (String) map.get("classes"),
                    (String) map.get("price"), (String) map.get("number"), (String) map.get("email"), (Location) map.get("location"));
        }
        return null;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (databaseReference != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        Iterable<DataSnapshot> snapshotIterator = snapshot.getChildren();
                        Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                        while (iterator.hasNext()) {
                            DataSnapshot next = (DataSnapshot) iterator.next();
                            Book book = next.getValue(Book.class);
                            Log.i(TAG, "Value = " + next.child("title").getValue());
                            books.add(book);
                        }
                        BookAdapter ba = new BookAdapter(Search.this, books);
                        mBooksRecycler.setAdapter(ba);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Search.this, error + TAG, Toast.LENGTH_SHORT).show();
                }
            });
        }

        mCurrentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void search(String s) {
        ArrayList<Book> myList = new ArrayList<>();
        for (Book object : books) {
            if (object.getTitle().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
        }
        BookAdapter ba = new BookAdapter(this, myList);
        mBooksRecycler.setAdapter(ba);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void onFilter(Filters filters) {
        // Construct query basic query
        Query query = databaseReference.orderByChild("listings");

        // Classes
        if (filters.hasClasses()) {
            query = query.equalTo(filters.getClasses());
        }

        // Price
        if (filters.hasPrice()) {
            query = query.equalTo(filters.getPrice());
        }

        // Price
        if (filters.hasLocation()) {
            query = query.equalTo(filters.getLocation());
        }

        // Sort by
        if (filters.hasSortBy()) {
            query = query.orderByChild(filters.getSortBy());
        }

        // Limit items
        query = query.limitToFirst(LIMIT);

        // Update the query
        mQuery = query;

        // Save filters
        mViewModel.setFilters(filters);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_bar:
                onFilterClicked();
                break;
            case R.id.button_clear_filter:
                onClearFilterClicked();
        }
    }

    public void onFilterClicked() {
        // Show the dialog containing filter options
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
    }

    public void onClearFilterClicked() {
        mFilterDialog.resetFilters();
        mCurrentSearch.setText("");
        onFilter(Filters.getDefault());
    }

    public void onBookSelected(DataSnapshot book) {
        // Go to the details page for the selected restaurant
        Intent intent = new Intent(this, ListingDetailActivity.class);
        intent.putExtra(ListingDetailActivity.KEY_BOOK_ID, ((Book) book.getValue()).getIsbn());

        startActivity(intent);
    }


    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // do something here
            Intent intent5 = new Intent(Search.this, LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent5);
        }
        /*else if (id == R.id.action_profile){
            Intent intent6 = new Intent(Search.this, Profile.class);
            startActivity(intent6);
        }*/
        return super.onOptionsItemSelected(item);
    }


}