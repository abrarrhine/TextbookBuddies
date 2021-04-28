package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

    TextView title, author, classes,isbn, price, email, phonenumber;
    Button cancel, submit, uploadimg;
    List<Book> oldbooklist;
    String userId;
    StorageReference storageReference;
    ImageView bookimg;

    private DatabaseReference firebaseDatabase;
    private DatabaseReference listingsRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Bitmap bitmap;
    Bitmap resource;

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

        bookimg = (ImageView) findViewById(R.id.iv_bookimg);
        uploadimg = (Button) findViewById(R.id.bt_uploadimg);

        bookimg.setImageBitmap(resource);
        bitmap= resource;

        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference bookref = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/booklist/"+bookimg.getImageAlpha()+"/bookimg.jpg");


        bookref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(bookimg);
            }
        });

        uploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

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
                        price.getText().toString(),
                        phonenumber.getText().toString(),
                        email.getText().toString(),
                        Location.ON_CAMPUS,
                        getImageUri(getApplicationContext(),bitmap).toString());

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
                // trying to make listings section
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);

            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        //Uploads image to Firebase Storage
        final StorageReference fileref = storageReference.child("users/"+firebaseAuth.getCurrentUser().getUid()+"/booklist/"+bookimg.getImageAlpha()+"/bookimg.jpg");
        fileref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddListing.this, "Book photo uploaded!", Toast.LENGTH_LONG).show();

                fileref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(bookimg);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddListing.this, "Sorry! Book photo was not uploaded!Try again!", Toast.LENGTH_LONG).show();
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

//    Source: https://stackoverflow.com/questions/42200448/how-to-get-uri-on-imageview-with-glide
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(AddListing.this.getContentResolver(), inImage, UUID.randomUUID().toString() + ".jpg", "drawing");
        return Uri.parse(path);
    }
}