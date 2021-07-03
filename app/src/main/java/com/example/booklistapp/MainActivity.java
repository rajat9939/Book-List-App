package com.example.booklistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mEmptyStateTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button search = (Button)findViewById(R.id.search_Book);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) findViewById(R.id.book_name);
                String BOOK_REQUEST_URL = text.getText().toString();

                Intent i = new Intent(MainActivity.this, bookActivity.class);
                i.putExtra("BOOK_URL", BOOK_REQUEST_URL);
                startActivity(i);
            }
        });
    }
}