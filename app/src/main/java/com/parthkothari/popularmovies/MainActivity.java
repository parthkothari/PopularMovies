package com.parthkothari.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String[] myDataset =  {"String1", "String2"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);



        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();

        String myUrl = "http://api.themoviedb.org/3/discover/movie";
        String result;

        try {
            result = getRequest.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public static class HttpGetRequest extends AsyncTask<String, Void, String>{
        private final String TAG = this.getClass().getSimpleName();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "On Pre Execute");

        }

        @Override
        protected String doInBackground(String... strings) {
            String results;
            Uri uri = new Uri.Builder()
                    .scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", BuildConfig.TMBDApiKey)
                    .build();
            Log.e(TAG, "Do In Background-" + uri.toString());
            Log.e(TAG, "Now Tring to get httpresults");
            try {
                results = getResponseFromHttpUrl(new URL(uri.toString()));
                Log.e(TAG, "Results " + results);
            } catch (IOException e) {
                Log.e(TAG, "IO Exception");
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "On Post Execute");
            Log.e(TAG, "On Post Execute, Trying to get the stored API" + BuildConfig.APPLICATION_ID);
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        String TAG = "getResponseFromHttpUrl";
        Log.e(TAG, "Trying to get response from http");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                Log.e(TAG, "Does have input");
                return scanner.next();
            } else {
                Log.e(TAG, "Does not have input");
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}




