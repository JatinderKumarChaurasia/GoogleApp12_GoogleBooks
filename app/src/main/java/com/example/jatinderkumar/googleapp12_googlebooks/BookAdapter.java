package com.example.jatinderkumar.googleapp12_googlebooks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jatinder Kumar on 10-03-2018.
 */

public class BookAdapter extends ArrayAdapter<Books> {
    public BookAdapter(@NonNull Context context, ArrayList<Books> books) {
        super(context, 0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View bookList = convertView;
        if (bookList == null)
        {
            bookList = LayoutInflater.from(getContext()).inflate(R.layout.book_list,parent,false);
        }
        Books books = getItem(position);
        TextView book_title = bookList.findViewById(R.id.book_title);
        TextView book_author = bookList.findViewById(R.id.book_author);
        TextView book_publisher = bookList.findViewById(R.id.book_publisher);
        book_title.setText(books.getTitle());
        book_author.setText(books.getAuthor());
        book_publisher.setText(books.getPublisher());
        return bookList;
    }
}
