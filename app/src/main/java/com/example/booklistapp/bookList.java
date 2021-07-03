package com.example.booklistapp;

public class bookList {
    private final String mTitle;
    private final String mAuthor;
    private final String mImageUrl;
    private final String mWebReaderLink;

    public bookList(String title, String author, String imageUrl, String webReaderLink) {
        mTitle = title;
        mAuthor = author;
        mImageUrl = imageUrl;
        mWebReaderLink = webReaderLink;

    }

    public String getAuthor() {
        return mAuthor;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getImageUrl(){
        return mImageUrl;
    }
    public String getWebReaderLink(){
        return getWebReaderLink();
    }

}
