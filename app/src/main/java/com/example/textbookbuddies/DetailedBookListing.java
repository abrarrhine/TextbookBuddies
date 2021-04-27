package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.search.Search;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

public class DetailedBookListing extends AppCompatActivity {

    TextView tv_name;
    TextView tv_isbn;
    TextView tv_class;
    TextView tv_contact;
    TextView tv_email;
    TextView tv_price;
    ImageView btn_back;

    TextView tv_logout;
    LinearLayout ll_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_book_listing);

        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_isbn = (TextView)findViewById(R.id.tv_isbn);
        tv_class = (TextView)findViewById(R.id.tv_class);
        tv_contact = (TextView)findViewById(R.id.tv_contact);
        tv_email = (TextView)findViewById(R.id.tv_email);
        tv_price = (TextView)findViewById(R.id.tv_price);
        btn_back = (ImageView) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        tv_logout = (TextView) findViewById(R.id.tv_logout);

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedBookListing.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedBookListing.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });

        Book book = Parcels.unwrap(getIntent().getParcelableExtra("book"));
        tv_name.setText(book.getTitle());
        tv_isbn.setText(book.getIsbn());
        tv_class.setText(book.getClasses());
        tv_contact.setText(book.getNumber());
        tv_email.setText(book.getEmail());
        tv_price.setText("$" + book.getPrice());

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