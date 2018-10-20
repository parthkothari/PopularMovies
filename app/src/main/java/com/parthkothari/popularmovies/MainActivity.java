package com.parthkothari.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.MovieCardClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Movie> myMovieDataset = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(myMovieDataset, this);
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

    @Override
    public void onMovieCardClick(int clickedItemIndex) {
        Toast.makeText(this, "I see that you clicked on item " + clickedItemIndex, Toast.LENGTH_SHORT).show();

        Intent intent  = new Intent(MainActivity.this, DetailActivity.class);
        startActivity(intent);

    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
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
            } catch (IOException e) {
                Log.e(TAG, "IO Exception");
                results = null;
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, "On Post Execute - Got results " + s);
            try {
                JSONObject resultJson = new JSONObject(s);
                JSONArray resultJsonArray = resultJson.getJSONArray("results");

                for (int i = 0; i < resultJsonArray.length(); i++) {
                    JSONObject jsonMovieObject = resultJsonArray.getJSONObject(i);
                    myMovieDataset.add(
                            new Movie(
                                    jsonMovieObject.getString("title")
                                    , jsonMovieObject.getString("poster_path")
                                    , jsonMovieObject.getInt("id")
                                    , jsonMovieObject.getDouble("vote_average")
                            )
                    );
                }
                Log.e(TAG, "Total Objects created-" + Integer.toString(myMovieDataset.size()));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}




