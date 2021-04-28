package com.example.textbookbuddies.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.textbookbuddies.DetailedBookListing;
import com.example.textbookbuddies.R;
import com.example.textbookbuddies.models.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    public Context context;
    public List<Book> books;
    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;
    public DatabaseReference firebaseDatabase;
    public String Uid;
    public static final String USER_INFO_URL = "https://textbook-buddies-31189-default-rtdb.firebaseio.com/users/";
    public static final String TAG = "BookAdapter";
    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Uid = firebaseUser.getUid();
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("BookAdapter", "onCreateViewHolder");
        View bookView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new ViewHolder((bookView));
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        Log.i("BookAdapter", "onBindViewHolder" + position);
        //Get the book at the passed position
        Book book = books.get(position);
        //Bind the book data into the VH
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, isbn, price, email, phonenumber;
        TextView authorTitle, isbnTitle, priceTitle, contactTitle;
       // ImageView bookimg;
        Button delete;
        RelativeLayout itemBook;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.bkTitle);
            author = v.findViewById(R.id.bkAuthor);
            isbn = v.findViewById(R.id.bkIsbn);
            price = v.findViewById(R.id.bkPrice);
            email = v.findViewById(R.id.bkEmail);
            phonenumber = v.findViewById(R.id.bkPhone);
            itemBook = v.findViewById(R.id.itembook);
          //  bookimg = v.findViewById(R.id.bkImage);
            delete = v.findViewById(R.id.btdelete);

            authorTitle = v.findViewById(R.id.bkAuthorTitle);
            isbnTitle = v.findViewById(R.id.bkIsbnTitle);
            priceTitle = v.findViewById(R.id.bkPriceTitle);
            contactTitle = v.findViewById(R.id.bkContactTitle);
        }

        public void bind(Book book) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            isbn.setText(book.getIsbn());
            price.setText("$"+book.getPrice());
            email.setText(book.getEmail());
            phonenumber.setText(book.getNumber());
           // Glide.with(context).load(book.getImage()).into(bookimg);

            authorTitle.setText("Author: ");
            isbnTitle.setText("ISBN: ");
            priceTitle.setText("Price: ");
            contactTitle.setText("Contact: ");

            itemBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, book.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, DetailedBookListing.class);
                    i.putExtra("book", Parcels.wrap(book));
                    context.startActivity(i);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    book.delete();
                }
            });
            //Glide.with(context).load(movie.getPosterPath()).into(ivPoster);
            //Glide.with(context).load(imageURL).into(ivPoster);


        }
    }
}
