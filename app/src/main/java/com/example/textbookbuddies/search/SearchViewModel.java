package com.example.textbookbuddies.search;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private Filters mFilters;

    public SearchViewModel() {
        mFilters = Filters.getDefault();
    }

    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}