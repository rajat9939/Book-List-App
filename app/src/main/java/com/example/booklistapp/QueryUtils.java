package com.example.booklistapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){
    }

    public static List<bookList> fetchBookData(String bUrl){
        URL url = createUrl(bUrl);

        String jsonResponse = "";
        try{
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<bookList> books = extractBooks(jsonResponse);
        return books;

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.i(LOG_TAG, "URL created...and string Url is " + url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<bookList> extractBooks(String jsonResponse) {
        List<bookList> books = new ArrayList<>();

        try{
            JSONObject jsonRootObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonRootObject.getJSONArray("items");
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject volumeInfoJsonObject = jsonObject.getJSONObject("volumeInfo");
                String title = volumeInfoJsonObject.getString("title");
                JSONArray jsonAuthor = volumeInfoJsonObject.getJSONArray("authors");
                String author = "";
                int k = 1;
                for(int j=0;j<jsonAuthor.length();j++)
                {
                    author = author + "  " + jsonAuthor.getString(j);
                    if(k<jsonAuthor.length())
                    {
                        author = author + ",";
                        k++;
                    }
                }

                JSONObject imageLinksJsonObject = volumeInfoJsonObject.getJSONObject("imageLinks");
                String imageUrl = imageLinksJsonObject.optString("smallThumbnail");

                JSONObject accessInfoJsonObject = jsonObject.getJSONObject("accessInfo");
                String webLink = accessInfoJsonObject.optString("webReaderLink");

                books.add(new bookList(title, author, imageUrl, webLink));
            }

        }
        catch (JSONException e)
        {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

}
