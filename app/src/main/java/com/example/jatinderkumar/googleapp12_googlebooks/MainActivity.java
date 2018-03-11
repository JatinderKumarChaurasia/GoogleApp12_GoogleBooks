package com.example.jatinderkumar.googleapp12_googlebooks;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView list;
    EditText searchText;
    TextView emptyView;
    ImageView imageView;
    ProgressBar progressBar;
    private static BookAdapter adapter;
    ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        searchButton = findViewById(R.id.search);
        emptyView = findViewById(R.id.noBooks);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        adapter = new BookAdapter(MainActivity.this,new ArrayList<Books>());
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books books = adapter.getItem(position);
                Uri url = Uri.parse(books.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW,url);
                startActivity(intent);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();

               ConnectivityManager connect = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = connect.getActiveNetworkInfo();
                if (info != null && info.isConnected())
                {
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute(getUrl());
                }else {
                    View loadingIndicator = findViewById(R.id.progress);
                    loadingIndicator.setVisibility(View.GONE);
                    emptyView.setText("No Internet Connection");
                    imageView.setImageResource(R.drawable.ic_error_outline_black_24dp);
                }
            }
        });
    }
    private String getUrl()
    {
        searchText = findViewById(R.id.searchBook);
        String title = searchText.getText().toString();
        String url ="https://www.googleapis.com/books/v1/volumes?q="+title;
        return url;
    }
    private  class BookAsyncTask extends AsyncTask<String,Void,List<Books>>
    {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Books> doInBackground(String... strings) {
            if (strings.length <1 || strings[0] == null)
            {
                return null;
            }
            List<Books> books = Utils.fetchDataFromUrl(strings[0]);
            return books;
        }
        @Override
        protected void onPostExecute(List<Books> books) {
            adapter.clear();
            progressBar.setVisibility(View.GONE);
            if (books != null && !books.isEmpty())
            {
                adapter.addAll(books);
            }
            else {
                emptyView.setText("No Book Found with this name");
                imageView.setImageResource(R.drawable.ic_chrome_reader_mode_black_24dp);
            }
        }
    }

}
