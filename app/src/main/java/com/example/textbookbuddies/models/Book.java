package com.example.textbookbuddies.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.textbookbuddies.R;


public class Book implements Comparable<Book>{

    public static final String  FIELD_TITLE = "title";
    public static final String  FIELD_ISBN = "isbn";
    public static final String  FIELD_AUTHOR = "author";
    public static final String  FIELD_CLASSES = "classes";
    public static final String  FIELD_PRICE = "price";
    public static final String  FIELD_NUMBER = "number";
    public static final String  FIELD_EMAIL = "email";

    private String title;
    private String isbn;
    private String author;
    private String classes;
    private double priceDouble;
    private String priceString;
    private String number;
    private String email;

    public Book(){

    }
    public Book(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("title");
        this.isbn = jsonObject.getString("isbn");
        this.author = jsonObject.getString("author");
        this.classes = jsonObject.getString("classes");
        this.priceDouble = jsonObject.getDouble("priceDouble");
        this.number = jsonObject.getString("number");
        this.email = jsonObject.getString("email");

    }

    public Book(String title, String isbn, String author, String classes, double price, String number, String email) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.classes = classes;
        this.priceDouble = price;
        this.number = number;
        this.email = email;
    }

    public static List<Book> fromJSONArray(JSONArray movieJsonArray) throws JSONException {
        List<Book> books = new ArrayList<>();
        for (int i=0; i<movieJsonArray.length(); i++){
            books.add(new Book(movieJsonArray.getJSONObject(i)));
        }
        return books;
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

    public int compareTo(Book book) {
        return this.title.compareTo(book.getTitle());
    }
}
