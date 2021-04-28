package com.example.textbookbuddies.models;

import android.net.Uri;
import android.util.Log;

import com.example.textbookbuddies.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
public class Book {

    public static final String TAG = "Book";
    public static final String  FIELD_TITLE = "title";
    public static final String  FIELD_ISBN = "isbn";
    public static final String  FIELD_AUTHOR = "author";
    public static final String  FIELD_CLASSES = "classes";
    public static final String  FIELD_PRICE = "price";
    public static final String FIELD_LOCATION = "location";
    public static final String  FIELD_NUMBER = "number";
    public static final String  FIELD_EMAIL = "email";

    String bookId;
    String title;
    String isbn;
    String author;
    String classes;
    String price;
    Location location;
    String number;
    String email;
    FirebaseUser firebaseUser;

    public Book(){

    }
    public Book(JSONObject jsonObject) throws JSONException {
        this.bookId = jsonObject.getString("bookId");
        this.title = jsonObject.getString("title");
        this.isbn = jsonObject.getString("isbn");
        this.author = jsonObject.getString("author");
        this.classes = jsonObject.getString("classes");
        this.price = jsonObject.getString("price");
        this.number = jsonObject.getString("number");
        this.email = jsonObject.getString("email");
    }

    public Book(String bookId, String title, String isbn, String author, String classes, String price, String number, String email, Location onCampus) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.classes = classes;
        this.price = price;
        this.number = number;
        this.email = email;
        this.location = location;
    }

    public static List<Book> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (int i=0; i<movieJsonArray.length(); i++){
            books.add(new Book(movieJsonArray.getJSONObject(i)));
        }
        return books;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return String.valueOf(number);
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void delete(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = firebaseUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listings = ref.child("listings").child(bookId);
        DatabaseReference userListings = ref.child("users").child(Uid).child("booklist").child(bookId);
        listings.removeValue();
        userListings.removeValue();
    }
}
