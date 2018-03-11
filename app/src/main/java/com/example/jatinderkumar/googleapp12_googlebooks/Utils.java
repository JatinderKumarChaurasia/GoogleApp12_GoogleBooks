package com.example.jatinderkumar.googleapp12_googlebooks;

import android.text.LoginFilter;
import android.text.TextUtils;
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

/**
 * Created by Jatinder Kumar on 10-03-2018.
 */

public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();
    private static String author_name,book_title,publisher,book_url;

    public Utils() {
    }
    private static URL createUrl(String requestUrl)
    {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e("Create Url Exception","Error in creating Url");
        }
        return url;
    }
    private static String makeHttpRequest(URL requestUrl) throws IOException {
        String jsonResponse = "";
        if (requestUrl == null)
        {
            return jsonResponse;
        }
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 200)
            {
                inputStream = connection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG,"Error Getting Request Code"+connection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e("Make Http Request Exception","Error in Making Http Request");
        }finally {
            if (connection != null)
            {
                connection.disconnect();
            }
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null)
        {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null)
            {
                builder.append(line);
                line = reader.readLine();
            }
        }
        return builder.toString();
    }

    private static List<Books> extractBookFromApi(String requestUrl)
    {
        if (TextUtils.isEmpty(requestUrl))
        {
            return null;
        }
        List<Books> books = new ArrayList<>();
        try {
            JSONObject baseResponse = new JSONObject(requestUrl);
            JSONArray items = baseResponse.getJSONArray("items");
            for (int i=0;i<items.length();i++)
            {
                JSONObject book = items.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                book_title = volumeInfo.getString("title");
                book_url = volumeInfo.getString("previewLink");
                JSONArray authors = volumeInfo.getJSONArray("authors");
                for (int j=0 ; j<authors.length();j++)
                {
                    author_name = authors.getString(j);
                }
                publisher = volumeInfo.getString("publisher");
                Books books1 = new Books(book_title,author_name,publisher,book_url);
                books.add(books1);
            }


        } catch (JSONException e) {
            Log.e("Utils", "Problem parsing the Google Api JSON results", e);
        }
        return books;
    }
    public static List<Books> fetchDataFromUrl(String requestUrl)
    {
        try {
            Thread.sleep(2000);
        }catch (InterruptedException e)
        {
            Log.e("Interupted Exception","It is Interupted");
        }
        URL url = createUrl(requestUrl);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Books> books = extractBookFromApi(jsonResponse);
        return books;
    }
}
