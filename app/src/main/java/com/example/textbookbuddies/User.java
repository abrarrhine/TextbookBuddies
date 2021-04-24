package com.example.textbookbuddies;

import java.util.ArrayList;

public class User {

    public String firstname, lastname, dateOfBirth, email, phonenumber;
    public ArrayList<Book> booklist = new ArrayList<Book>();

    public User(){

    }
    public User(String firstname, String lastname, String dateOfBirth, String email, String phonenumber){
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public User(String firstname, String lastname, String dateOfBirth, String email, String phonenumber,ArrayList<Book> booklist){
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phonenumber = phonenumber;
        this.booklist = booklist;
    }



}
