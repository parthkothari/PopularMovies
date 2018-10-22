package com.parthkothari.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        mLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.poster_grid_columns));
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerViewAdapter(myMovieDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        loadMovieData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String selectedOption = item.toString();
        Toast.makeText(this, selectedOption, Toast.LENGTH_SHORT).show();
        loadMovieData(selectedOption);
        return true;
    }

    @Override
    public void onMovieCardClick(Movie clickedMovie) {
        Toast.makeText(this, "I see that you clicked on movie " + clickedMovie.getmTitle(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("title", clickedMovie.getmTitle());
        intent.putExtra("overview", clickedMovie.getmOverview());
        intent.putExtra("backdropPath", clickedMovie.getmBackdropPath());
        intent.putExtra("releaseDate", clickedMovie.getmReleaseDate());
        intent.putExtra("posterPath", clickedMovie.getmPosterPath());
        intent.putExtra("averageVote", clickedMovie.getmAverageVote());
        startActivity(intent);

    }

    public void loadMovieData(String... args) {
        String sortCriteria = new String();

        if (args.length > 0){
            sortCriteria = args[0];
        }

        String myUrl = buildUri(sortCriteria).toString();

        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            getRequest.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Uri buildUri(String sortCriteria) {
        Uri.Builder builder = new Uri.Builder()
                .scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3");

        switch (sortCriteria) {
            case "Popularity":
                builder.appendPath("movie").appendPath("popular");
                break;

            case "Top Rated":
                builder.appendPath("movie").appendPath("top_rated");
                break;
            default:
                builder.appendPath("discover").appendPath("movie");
        }

        builder.appendQueryParameter("api_key", BuildConfig.TMBDApiKey);
        return builder.build();
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
            String resourceEndpoint = strings[0];

            try {
                results = getResponseFromHttpUrl(new URL(resourceEndpoint));
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
            if (s == null){
                Toast.makeText(MainActivity.this, "Unable to fetch data!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject resultJson = new JSONObject(s);
                JSONArray resultJsonArray = resultJson.getJSONArray("results");
                myMovieDataset.clear();

                for (int i = 0; i < resultJsonArray.length(); i++) {
                    JSONObject jsonMovieObject = resultJsonArray.getJSONObject(i);
                    myMovieDataset.add(
                            new Movie(
                                    jsonMovieObject.getString("title")
                                    , jsonMovieObject.getString("poster_path")
                                    , jsonMovieObject.getInt("id")
                                    , jsonMovieObject.getDouble("vote_average")
                                    , jsonMovieObject.getString("overview")
                                    , jsonMovieObject.getString("release_date")
                                    , jsonMovieObject.getString("backdrop_path")
                            )
                    );
                }
                Log.e(TAG, "Total Objects created-" + Integer.toString(myMovieDataset.size()));
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}




