package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.search.Search;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailedBookListing extends AppCompatActivity {

    TextView tv_name;
    TextView tv_isbn;
    TextView tv_class;
    TextView tv_contact;
    TextView tv_email;
    TextView tv_price;
    ImageView iv_photo;
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
        iv_photo = (ImageView) findViewById(R.id.detailed_bkImage);
        btn_back = (ImageView) findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_logout = (TextView) findViewById(R.id.tv_logout);

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedBookListing.this, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
            }
        });

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            ConstraintLayout ll_logout;
            ll_logout = (ConstraintLayout) findViewById(R.id.ll_logout);
            ll_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailedBookListing.this, LoginActivity.class);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(intent);
                }
            });
        } else {
            // In portrait
            LinearLayout ll_logout;
            ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
            ll_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailedBookListing.this, LoginActivity.class);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(intent);
                }
            });
        }

        Book book = Parcels.unwrap(getIntent().getParcelableExtra("book"));

        if (!book.getPhoto().equals("none")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference profileref = storageReference.child("images/"+book.getPhoto()+"/bookImage.jpg");
            profileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(iv_photo.getContext()).load(uri).into(iv_photo);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }
        tv_name.setText(book.getTitle());
        tv_isbn.setText(book.getIsbn());
        tv_class.setText(book.getClasses());
        tv_contact.setText(book.getNumber());
        tv_email.setText(book.getEmail());
        tv_price.setText(book.getPriceString());

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