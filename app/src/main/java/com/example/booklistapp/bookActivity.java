package com.example.booklistapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager.LoaderCallbacks;
import java.util.ArrayList;
import java.util.List;

public class bookActivity extends AppCompatActivity  implements LoaderCallbacks<List<bookList>>{

    private static String BOOK_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final int BOOK_LOADER_ID = 1;
    private static final String LOG_TAG = bookActivity.class.getName();
    private TextView mEmptyStateTextView;
    private BookAdapter bAdapter;
    private static String NEW_BOOK_URL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Log.i(LOG_TAG, "bookActivity.java file is opened");


        final ListView bookListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_List);
        bookListView.setEmptyView(mEmptyStateTextView);

        bAdapter = new BookAdapter(bookActivity.this, new ArrayList<bookList>());
        bookListView.setAdapter(bAdapter);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork !=null && activeNetwork.isConnectedOrConnecting())
        {
            Log.i(LOG_TAG, "Test: calling initLoader...");
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        }
        else
        {
            Log.i(LOG_TAG, "Test: No network Found");
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_Network);
        }



    }

    @Override
    public Loader<List<bookList>> onCreateLoader(int id, Bundle args) {

        Log.i(LOG_TAG, "Test: onCreateLoader() called... Loader is initialized and created for the given URL");
        Intent intent = getIntent();
        String BOOK_QUERY = intent.getStringExtra("BOOK_URL");

        Uri baseUri = Uri.parse(BOOK_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", BOOK_QUERY);
        return new BooksLoader(bookActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<bookList>> loader, List<bookList> data) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        Log.e(LOG_TAG, "Test: onLoadFinished()... Previously created loader has finished working");

        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        if(activeNetwork==null)
        {
            mEmptyStateTextView.setText(R.string.no_Network);
        }
        else if(activeNetwork!=null && activeNetwork.isConnected())
        {
            bAdapter.clear();
            if(data != null && !data.isEmpty())
                bAdapter.addAll(data);
        }
        mEmptyStateTextView.setText(R.string.no_Book);

    }

    @Override
    public void onLoaderReset(Loader<List<bookList>> loader) {

        Log.e(LOG_TAG, "Test: onLoaderReset()... Previously created loader has been reset");
        NEW_BOOK_URL = null;
        bAdapter.clear();

    }
}