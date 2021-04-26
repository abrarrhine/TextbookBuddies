package com.example.textbookbuddies.search;
import android.content.Context;
import android.text.TextUtils;

import com.example.textbookbuddies.R;
import com.example.textbookbuddies.models.Book;
import com.google.firebase.firestore.Query;

/**
 * Object for passing filters around.
 */
public class Filters {

    private int price = -1;
    private String classes = null;
    private String location = null;
    private String sortBy = null;
    private Query.Direction sortDirection = null;

    public Filters() {}

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortDirection(Query.Direction.DESCENDING);

        return filters;
    }


    public boolean hasPrice() {
        return (price > 0);
    }

    public boolean hasClasses() {
        return classes == null;
    }

    public boolean hasLocation() {
        return location == null;
    }

    public boolean hasSortBy() {
        return !(TextUtils.isEmpty(sortBy));
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getClasses() {
        return classes;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Query.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Query.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

}