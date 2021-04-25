package com.example.textbookbuddies;

public class Book {

    private String title;
    private String isbn;
    private String author;
    private String classes;
    private double price;
    private int number;
    private String email;

    public Book(){

    }
    public Book( String title, String isbn, String author, String classes, double price, int number, String email){
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.classes = classes;
        this.price = price;
        this.number = number;
        this.email = email;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
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

}
