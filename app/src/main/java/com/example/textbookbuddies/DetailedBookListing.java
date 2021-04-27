package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.search.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

public class DetailedBookListing extends AppCompatActivity {

    TextView tv_name;
    TextView tv_isbn;
    TextView tv_class;
    TextView tv_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_book_listing);

        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_isbn = (TextView)findViewById(R.id.tv_isbn);
        tv_class = (TextView)findViewById(R.id.tv_class);
        tv_contact = (TextView)findViewById(R.id.tv_contact);

        Book book = Parcels.unwrap(getIntent().getParcelableExtra("book"));
        tv_name.setText(book.getTitle());
        tv_isbn.setText(book.getIsbn());
        tv_class.setText(book.getClasses());
        tv_contact.setText(book.getNumber());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.ic_search:
                        Intent intent1 = new Intent(DetailedBookListing.this, Search.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_home:
                        Intent intent2 = new Intent(DetailedBookListing.this, Profile.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_listings:
                        Intent intent3 = new Intent(DetailedBookListing.this, Listings.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_help:
                        Intent intent4 = new Intent(DetailedBookListing.this, FAQ.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }
}