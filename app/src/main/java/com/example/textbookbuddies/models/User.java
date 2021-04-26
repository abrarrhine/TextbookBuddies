package com.example.textbookbuddies.models;

import java.util.ArrayList;

public class User {

    public String firstname, lastname, dateOfBirth, email, phonenumber;
    public ArrayList<Book> booklist = new ArrayList<Book>();

    public User(){

    }
//    public User(String firstname, String lastname, String dateOfBirth, String email, String phonenumber){
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.dateOfBirth = dateOfBirth;
//        this.email = email;
//        this.phonenumber = phonenumber;
//    }

    public User(String firstname, String lastname, String dateOfBirth, String email, String phonenumber,ArrayList<Book> booklist){
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phonenumber = phonenumber;
        this.booklist = booklist;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public ArrayList<Book> getBooklist() {
        return booklist;
    }

    public void setBooklist(ArrayList<Book> booklist) {
        this.booklist = booklist;
    }
}
