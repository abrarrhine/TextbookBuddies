package com.example.textbookbuddies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.TextView;
import com.example.textbookbuddies.models.Book;
import com.example.textbookbuddies.search.*;
import com.example.textbookbuddies.ui.login.LoginActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
    ImageView image;
    Button cancel, submit, upload;
    List<Book> oldbooklist;
    String userId;

    private DatabaseReference firebaseDatabase;
    private DatabaseReference listingsRef;
    private DatabaseReference userBookListRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    String photo = "none";
    Bitmap bitmap;
    public static final int GET_FROM_GALLERY = 3;

    Book newbook;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        listingsRef = FirebaseDatabase.getInstance().getReference().child("listings");
        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        userBookListRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("booklist");
        oldbooklist = new ArrayList<>();
        title = findViewById(R.id.bkAddtitle);
        author = findViewById(R.id.bkAddauthor);
        classes = findViewById(R.id.bkAddclasses);
        isbn = findViewById(R.id.bkAddisbn);
        price = findViewById(R.id.bkAddprice);
        email = findViewById(R.id.bkAddemail);
        phonenumber = findViewById(R.id.bkAddphonenumber);
        image = findViewById(R.id.iv_bookimg);
        cancel = findViewById(R.id.btcancel);
        submit = findViewById(R.id.btsubmit);
        upload = findViewById(R.id.bt_uploadimg);

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

                // trying to make listings section
                listingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        key = listingsRef.push().getKey();
                        newbook = new Book(
                                key,
                                title.getText().toString(),
                                isbn.getText().toString(),
                                author.getText().toString(),
                                classes.getText().toString(),
                                Double.valueOf(price.getText().toString()),
                                phonenumber.getText().toString(),
                                email.getText().toString(),
                                photo);
                        listingsRef.child(key).setValue(newbook);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                userBookListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userBookListRef.child(key).setValue(newbook);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                //end

                Intent submitIntent = new Intent(AddListing.this, Listings.class);
                startActivity(submitIntent);
            }
        });

        storageReference = FirebaseStorage.getInstance().getReference().child("images/pic1.jpg");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] bytedata = baos.toByteArray();

                UploadTask uploadTask = storageReference.putBytes(bytedata);
                final StorageReference ref = storageReference;
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            photo = task.getResult().toString();
                        }
                    }
                });
                image.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}