package com.example.textbookbuddies.models;

import android.util.Log;

import com.example.textbookbuddies.AddListing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.textbookbuddies.R;


public class Book implements Comparable<Book>, Parcelable {

    public static final String TAG = "Book";
    public static final String  FIELD_BOOKID = "bookId";
    public static final String  FIELD_TITLE = "title";
    public static final String  FIELD_ISBN = "isbn";
    public static final String  FIELD_AUTHOR = "author";
    public static final String  FIELD_CLASSES = "classes";
    public static final String  FIELD_PRICE = "price";
    public static final String  FIELD_NUMBER = "number";
    public static final String  FIELD_EMAIL = "email";
    public static final String  FIELD_IMGURI = "imgUri";

    String bookId;
    FirebaseUser firebaseUser;
    private String title;
    private String isbn;
    private String author;
    private String classes;
    private double priceDouble;
    private String priceString;
    private String number;
    private String email;
    private String photo = "none";

    public Book(){

    }
    public Book(JSONObject jsonObject) throws JSONException {
        this.bookId = jsonObject.getString("bookId");
        this.title = jsonObject.getString("title");
        this.isbn = jsonObject.getString("isbn");
        this.author = jsonObject.getString("author");
        this.classes = jsonObject.getString("classes");
        this.priceDouble = jsonObject.getDouble("priceDouble");
        this.number = jsonObject.getString("number");
        this.email = jsonObject.getString("email");
        this.photo = jsonObject.getString("imgUri");

    }

    public Book(String bookId,String title, String isbn, String author, String classes, double priceDouble, String number, String email, String photo) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.classes = classes;
        this.priceDouble = priceDouble;
        this.number = number;
        this.email = email;
        this.photo = photo;
    }

    public static List<Book> fromJSONArray(JSONArray bookJsonArray) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (int i=0; i<bookJsonArray.length(); i++){
            books.add(new Book(bookJsonArray.getJSONObject(i)));
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

    public String getPriceString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(priceDouble);
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public double getPriceDouble() {
        return priceDouble;
    }
    public void setPriceDouble(double price) {
        this.priceDouble = price;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String imgUri) {
        this.photo = imgUri;
    }

    public int compareTo(Book book) {
        return this.title.compareTo(book.getTitle());
    }
    public void delete() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String Uid = firebaseUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listings = ref.child("listings").child(bookId);
        DatabaseReference userListings = ref.child("users").child(Uid).child("booklist").child(bookId);
        listings.removeValue();
        userListings.removeValue();

    }

    protected Book(Parcel in) {
        title = in.readString();
        isbn = in.readString();
        author = in.readString();
        classes = in.readString();
        priceDouble = in.readDouble();
        priceString = in.readString();
        number = in.readString();
        email = in.readString();
        photo = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(isbn);
        dest.writeString(author);
        dest.writeString(classes);
        dest.writeDouble(priceDouble);
        dest.writeString(priceString);
        dest.writeString(number);
        dest.writeString(email);
        dest.writeString(photo);
    }
}