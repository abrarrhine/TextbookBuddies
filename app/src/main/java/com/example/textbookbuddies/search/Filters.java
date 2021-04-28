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

    private String searchBy = null;
    private String sortBy = null;

    public Filters() {}

    public static Filters getDefault() {
        Filters filters = new Filters();
        return filters;
    }


    public boolean hasSearchBy() {
        return (searchBy != null);
    }
    public String getSearchBy() {
        if (searchBy.contains("class")) {
            return "classes";
        }
        return searchBy;
    }
    public void setSearchBy(String str) { this.searchBy = str;}

    public boolean hasSortBy() {
        return (sortBy != null);
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBY) {
        this.sortBy = sortBy;
    }
}