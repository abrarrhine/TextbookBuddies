package com.example.textbookbuddies.search;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.textbookbuddies.R;
import com.example.textbookbuddies.models.Book;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class ListingDetailActivity extends AppCompatActivity implements
        View.OnClickListener,
        EventListener<DocumentSnapshot> {

    private static final String TAG = "BookDetail";

    public static final String KEY_BOOK_ID = "key_book_id";

    private ImageView mImageView;
    private TextView mNameView;
    private TextView mNumRatingsView;
    private TextView mCityView;
    private TextView mCategoryView;
    private TextView mPriceView;
    private ViewGroup mEmptyView;
    private RecyclerView mRatingsRecycler;


    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DocumentReference bookRef;
    private ListenerRegistration mRestaurantRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        mImageView = findViewById(R.id.book_image);
        mNameView = findViewById(R.id.book_name);
        mCityView = findViewById(R.id.book_isbn);
//        mCategoryView = findViewById(R.id.book_classes);
//        mPriceView = findViewById(R.id.restaurant_price);
//
//        findViewById(R.id.restaurant_button_back).setOnClickListener(this);

        // Get restaurant ID from extras
        String bookId = getIntent().getExtras().getString(KEY_BOOK_ID);
        if (bookId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_BOOK_ID);
        }

        // Initialize Firestore
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Get reference to the restaurant
        databaseReference = firebaseDatabase.getReference("listings");

        //bookRef = databaseReference.getValue(bookId);

    }

    @Override
    public void onStart() {
        super.onStart();

        mRestaurantRegistration = bookRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mRestaurantRegistration != null) {
            mRestaurantRegistration.remove();
            mRestaurantRegistration = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_button_back:
                onBackArrowClicked(v);
                break;
        }
    }


    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }

        onBookLoaded(snapshot.toObject(Book.class));
    }

    private void onBookLoaded(Book book) {
        mNameView.setText(book.getTitle());
        mCityView.setText(book.getAuthor());
        mCategoryView.setText(book.getClasses());
        mPriceView.setText(book.getPriceString());

        // Background image
//        Glide.with(mImageView.getContext())
//                .load(book.getPhoto())
//                .into(mImageView);
    }

    public void onBackArrowClicked(View view) {
        onBackPressed();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}