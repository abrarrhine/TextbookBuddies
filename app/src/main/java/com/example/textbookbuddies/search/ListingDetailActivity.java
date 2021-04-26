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

import com.bumptech.glide.Glide;
import com.example.textbookbuddies.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ListingDetailActivity extends AppCompatActivity implements
        View.OnClickListener,
        EventListener<DocumentSnapshot> {

    private static final String TAG = "ListingDetail";

    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";

    private ImageView mImageView;
    private TextView mNameView;
    private TextView mNumRatingsView;
    private TextView mCityView;
    private TextView mCategoryView;
    private TextView mPriceView;
    private ViewGroup mEmptyView;
    private RecyclerView mRatingsRecycler;


    private FirebaseFirestore mFirestore;
    private DocumentReference mRestaurantRef;
    private ListenerRegistration mRestaurantRegistration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        mImageView = findViewById(R.id.restaurant_image);
        mNameView = findViewById(R.id.restaurant_name);
        mNumRatingsView = findViewById(R.id.restaurant_num_ratings);
        mCityView = findViewById(R.id.restaurant_city);
        mCategoryView = findViewById(R.id.restaurant_category);
        mPriceView = findViewById(R.id.restaurant_price);

        findViewById(R.id.restaurant_button_back).setOnClickListener(this);

        // Get restaurant ID from extras
        String restaurantId = getIntent().getExtras().getString(KEY_RESTAURANT_ID);
        if (restaurantId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_RESTAURANT_ID);
        }

        // Initialize Firestore
        mFirestore = FirebaseUtil.getFirestore();

        // Get reference to the restaurant
        mRestaurantRef = mFirestore.collection("restaurants").document(restaurantId);

    }

    @Override
    public void onStart() {
        super.onStart();

        mRestaurantRegistration = mRestaurantRef.addSnapshotListener(this);
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
            case R.id.restaurant_button_back:
                onBackArrowClicked(v);
                break;
        }
    }


    /**
     * Listener for the Restaurant document ({@link #mRestaurantRef}).
     */
    @Override
    public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }

        onRestaurantLoaded(snapshot.toObject(Listing.class));
    }

    private void onRestaurantLoaded(Listing listing) {
        mNameView.setText(listing.getTitle());
        mCityView.setText(listing.getLocation());
        mCategoryView.setText(listing.getSubject());
        mPriceView.setText(ListingUtil.getPriceString(listing.getPrice()));

        // Background image
        Glide.with(mImageView.getContext())
                .load(listing.getPhoto())
                .into(mImageView);
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