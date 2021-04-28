package com.example.textbookbuddies.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.textbookbuddies.DetailedBookListing;
import com.example.textbookbuddies.R;
import com.example.textbookbuddies.models.Book;

import org.parceler.Parcels;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    Context context;
    List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        this.context = context;
        this.books = books;
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
        }

        public void bind(Book book) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
            isbn.setText(book.getIsbn());
            price.setText(book.getPriceString());
            email.setText(book.getEmail());
            phonenumber.setText(book.getNumber());

            itemBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, book.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, DetailedBookListing.class);
                    i.putExtra("book", Parcels.wrap(book));
                    context.startActivity(i);
                }
            });

            //Glide.with(context).load(movie.getPosterPath()).into(ivPoster);
            //Glide.with(context).load(imageURL).into(ivPoster);


        }
    }
}


//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView tvTitle;
//        TextView tvOverview;
//        ImageView ivPoster;
//        RelativeLayout itemmovie;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvOverview = itemView.findViewById(R.id.tvOverview);
//            ivPoster = itemView.findViewById(R.id.ivPoster);
//            itemmovie = itemView.findViewById(R.id.itemmovie);
//            setMode(isDark);
//        }
//
//        public void bind(Movie movie) {
//            tvTitle.setText(movie.getTitle());
//            tvOverview.setText(movie.getOverview());
//
//            String imageUrl;
//            //if phone is in landscape mode
//            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//                //then imageUrl = backdrop img
//
//                imageUrl = movie.getBackdropPath();
//            }else{
//                //else imageUrl = poster images
//
//                imageUrl = movie.getPosterPath();
//            }
//            Glide.with(context).load(imageUrl).into(ivPoster);
//
//            //set click listener to whole movie item
//            itemmovie.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //navigate to a new activity by tapping on movie
//                    Intent i = new Intent(context, DetailActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    i.putExtra("movie", Parcels.wrap(movie));
//                    i.putExtra("isDark", isDark);
//                    context.startActivity(i);
//                    Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//}
