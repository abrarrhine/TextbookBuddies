package com.example.textbookbuddies.search;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.textbookbuddies.R;
import com.example.textbookbuddies.models.Book;
import com.google.firebase.firestore.Query;

/**
 * Dialog Fragment containing filter form.
 */
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "FilterDialog";

    interface FilterListener {

        void onFilter(Filters filters);

    }

    private View mRootView;

    private Spinner spinnerSearch;
    private Spinner spinnerSort;

    private FilterListener mFilterListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filters, container, false);

        spinnerSearch = mRootView.findViewById(R.id.spinner_search);
        spinnerSort = mRootView.findViewById(R.id.spinner_sort);

        mRootView.findViewById(R.id.button_search).setOnClickListener(this);
        mRootView.findViewById(R.id.button_cancel).setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_search:
                onSearchClicked();
                break;
            case R.id.button_cancel:
                onCancelClicked();
                break;
        }
    }

    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFilters());
        }

        dismiss();
    }

    public void onCancelClicked() {
        dismiss();
    }

    private String getSelectedSearch() {
        String selected = (String) spinnerSearch.getSelectedItem();
        return selected;
    }

    private String getSelectedSort() {
        String selected = (String) spinnerSort.getSelectedItem();
        return selected;
    }

    public void resetFilters() {
        if (mRootView != null) {
            spinnerSearch.setSelection(0);
            spinnerSort.setSelection(0);
        }
    }

    public Filters getFilters() {
        Filters filters = new Filters();

        if (mRootView != null) {
            filters.setSearchBy(getSelectedSearch());
            filters.setSortBy(getSelectedSort());
        }
        return filters;
    }
}