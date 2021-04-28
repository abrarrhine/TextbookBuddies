package com.example.textbookbuddies.models;

import java.util.Comparator;

public class BookPriceCompare implements Comparator<Book> {
    public int compare(Book b1, Book b2) {
        if(b1.getPriceDouble() < b2.getPriceDouble()) return -1;
        if(b1.getPriceDouble() > b2.getPriceDouble()) return 1;
        else return 0;
    }
}
