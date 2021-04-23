package com.example.textbookbuddies;

public class User {

    public String firstname, lastname, dateOfBirth, email, phonenumber;

    public User(){

    }
    public User(String firstname, String lastname, String dateOfBirth, String email, String phonenumber){
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phonenumber = phonenumber;
    }

}
