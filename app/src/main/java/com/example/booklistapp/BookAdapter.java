package com.example.booklistapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.List;


public class BookAdapter extends ArrayAdapter<bookList>{

    private final static String ADD_BY = "By";
    private static final String LOG_TAG = BookAdapter.class.getSimpleName();
    Context bContext;
    public BookAdapter(Context context, List<bookList> objects) {
        super(context, 0, objects);
        bContext = context;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View listItemView = convertView;
        bookList books = getItem(position);
        if(convertView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list, parent, false);
        }

        TextView book_title = (TextView) listItemView.findViewById(R.id.title_text_view);
        book_title.setText(books.getTitle());

        TextView book_author = (TextView) listItemView.findViewById(R.id.author_text_view);
        String autorName = ADD_BY + books.getAuthor();
        book_author.setText(autorName);

        ImageView book_image = (ImageView) listItemView.findViewById(R.id.image);
        Log.i(LOG_TAG, "Loading image..." + books.getImageUrl());
        Glide.with(bContext)
                .load(books.getImageUrl())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(book_image);


        return listItemView;
    }
}
