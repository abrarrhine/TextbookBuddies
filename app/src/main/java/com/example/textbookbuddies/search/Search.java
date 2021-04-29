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
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.textbookbuddies.BottomNavigationViewHelper;
import com.example.textbookbuddies.FAQ;
import com.example.textbookbuddies.Listings;
import com.example.textbookbuddies.Profile;
import com.example.textbookbuddies.R;
import com.example.textbookbuddies.adapters.*;
import com.example.textbookbuddies.models.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Search extends AppCompatActivity implements
        View.OnClickListener,
        FilterDialogFragment.FilterListener {

    private static final String TAG = "Search";

    private static final int LIMIT = 50;

    private Toolbar toolbar;
    private EditText currentSearch;
    private RecyclerView booksRecycler;
    private ViewGroup emptyView;

    private DatabaseReference databaseReference;

    private FilterDialogFragment mFilterDialog;
    private BookAdapter bookAdapter;

    private List<Book> books;

    private SearchViewModel mViewModel;
    private RecyclerView mBooksRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentSearch = (EditText) findViewById(R.id.text_current_search);
        booksRecycler = findViewById(R.id.recycler_books);
        booksRecycler.setLayoutManager(new LinearLayoutManager(this));
        emptyView = findViewById(R.id.view_empty);

        findViewById(R.id.filter_bar).setOnClickListener(this);
        findViewById(R.id.button_clear_filter).setOnClickListener(this);

        // View model
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        Filters f = new Filters();
        f.setSearchBy("title");
        f.setSortBy("title");
        mViewModel.setFilters(f);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("listings");
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
                        books.clear();
                        while (iterator.hasNext()) {
                            DataSnapshot next = (DataSnapshot) iterator.next();
                            Book book = next.getValue(Book.class);
                            Log.i(TAG, "Value = " + next.child("title").getValue());
                            books.add(book);
                        }
                        BookTitleCompare btc = new BookTitleCompare();
                        Collections.sort(books, btc);

                        BookAdapter ba = new BookAdapter(Search.this, books);
                        booksRecycler.setAdapter(ba);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Search.this, error + TAG, Toast.LENGTH_SHORT).show();
                }
            });
        }

        currentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString(), mViewModel.getFilters());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        onFilter(mViewModel.getFilters());
    }

    public void onStop() {
        super.onStop();
    }

    public void search(String s, Filters filters) {
        ArrayList<Book> myList = new ArrayList<>();
        for (Book object : books) {
            if (filters.hasSearchBy()) {
                if (filters.getSearchBy().equals("title") && object.getTitle().toLowerCase().contains(s.toLowerCase())) {
                    myList.add(object);
                }
                if (filters.getSearchBy().equals("isbn") && object.getIsbn().toLowerCase().contains(s.toLowerCase())) {
                    myList.add(object);
                }
                if (filters.getSearchBy().equals("classes") && object.getClasses().toLowerCase().contains(s.toLowerCase())) {
                    myList.add(object);
                }
            }

        }
        if (filters.hasSortBy()) {
            if (filters.getSortBy().equals("price")) {
                BookPriceCompare bpc = new BookPriceCompare();
                Collections.sort(myList, bpc);

            } else {
                BookTitleCompare btc = new BookTitleCompare();
                Collections.sort(myList, btc);
            }
        }
        BookAdapter ba = new BookAdapter(this, myList);
        booksRecycler.setAdapter(ba);
    }

    @Override
    public void onFilter(Filters filters) {
        search(currentSearch.getText().toString(), filters);
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
                Filters f = new Filters();
                f.setSearchBy("title");
                f.setSortBy("title");
                onFilter(f);
        }
    }

    public void onFilterClicked() {
        // Show the dialog containing filter options
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
    }
}