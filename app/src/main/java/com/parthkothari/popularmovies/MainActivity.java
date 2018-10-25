package com.parthkothari.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
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

        Button mReloadBtn = findViewById(R.id.btn_reload);
        mReloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMovieData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String selectedOption = item.toString();
        Toast.makeText(this, "Sorting movies by " + selectedOption, Toast.LENGTH_SHORT).show();
        loadMovieData(selectedOption);
        return true;
    }

    @Override
    public void onMovieCardClick(Movie clickedMovie) {
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
        String sortCriteria = "";

        if (args.length > 0) {
            sortCriteria = args[0];
        }

        String myUrl = buildUri(sortCriteria).toString();

        HttpGetRequest getRequest = new HttpGetRequest();
        try {
            getRequest.execute(myUrl).get();
        } catch (ExecutionException | InterruptedException e) {
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

    public void showPosterGrid() {
        RecyclerView movieData = findViewById(R.id.rv_movie_list);
        LinearLayout errorMessage = findViewById(R.id.ll_error_view);
        LinearLayout progressView = findViewById(R.id.ll_load_view);

        movieData.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
    }

    public void showErrorMessage() {
        RecyclerView movieData = findViewById(R.id.rv_movie_list);
        LinearLayout errorMessage = findViewById(R.id.ll_error_view);
        LinearLayout progressView = findViewById(R.id.ll_load_view);

        movieData.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

    public void showLoadingMessage(){
        RecyclerView movieData = findViewById(R.id.rv_movie_list);
        LinearLayout progressView = findViewById(R.id.ll_load_view);

        movieData.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        private final String TAG = this.getClass().getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingMessage();
        }

        @Override
        protected String doInBackground(String... strings) {
            String results;
            String resourceEndpoint = strings[0];

            try {
                results = getResponseFromHttpUrl(new URL(resourceEndpoint));
            } catch (IOException e) {
                results = null;
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                showErrorMessage();
                Toast.makeText(MainActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                showPosterGrid();
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
                mRecyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                showErrorMessage();
                e.printStackTrace();
            }
        }
    }

}






