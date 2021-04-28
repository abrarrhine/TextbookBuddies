package com.example.textbookbuddies.models;

import android.net.Uri;

import com.example.textbookbuddies.Location;

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

    public static final String  FIELD_TITLE = "title";
    public static final String  FIELD_ISBN = "isbn";
    public static final String  FIELD_AUTHOR = "author";
    public static final String  FIELD_CLASSES = "classes";
    public static final String  FIELD_PRICE = "price";
    public static final String FIELD_LOCATION = "location";
    public static final String  FIELD_NUMBER = "number";
    public static final String  FIELD_EMAIL = "email";
    public static final String FIELD_IMAGE = "image";

    private String title;
    private String isbn;
    private String author;
    private String classes;
    private String price;
    private Location location;
    private String number;
    private String email;
    private String image;

    public Book(){

    }
    public Book(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("title");
        this.isbn = jsonObject.getString("isbn");
        this.author = jsonObject.getString("author");
        this.classes = jsonObject.getString("classes");
        this.price = jsonObject.getString("price");
        this.number = jsonObject.getString("number");
        this.email = jsonObject.getString("email");
        this.image = jsonObject.getString("image");
    }

//    public Book(String title, String isbn, String author, String classes, String price, String number, String email, Location location, int image) {
//        this.title = title;
//        this.isbn = isbn;
//        this.author = author;
//        this.classes = classes;
//        this.price = price;
//        this.number = number;
//        this.email = email;
//        this.location = location;
//        this.image = image;
//    }

    public Book(String title, String isbn, String author, String classes, String price, String number, String email, Location onCampus, String image) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.classes = classes;
        this.price = price;
        this.number = number;
        this.email = email;
        this.location = location;
        this.image = image;
    }

    public static List<Book> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (int i=0; i<movieJsonArray.length(); i++){
            books.add(new Book(movieJsonArray.getJSONObject(i)));
        }
        return books;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("author", author);
        result.put("isbn", isbn);
        result.put("classes", classes);
        result.put("price", price);
        result.put("number", number);
        result.put("email", email);
        result.put("location", location);
        result.put("image", image);
        return result;
    }
}
