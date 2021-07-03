package com.example.booklistapp;

import android.content.Context;
import android.util.Log;
import android.content.AsyncTaskLoader;
import java.util.List;

public class BooksLoader extends AsyncTaskLoader<List<bookList>> {

    private String bUrl;
    public static final String LOG_TAG = BooksLoader.class.getName();
    public BooksLoader(Context context, String url) {
        super(context);
        bUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG , "Test: onStartLoading()... ");
        forceLoad();
    }

    @Override
    public List<bookList> loadInBackground() {

        Log.i(LOG_TAG , "Test: loadInBackground()... ");
        if(bUrl==null)
            return null;

        List<bookList> books = QueryUtils.fetchBookData(bUrl);
        return books;
    }
}
