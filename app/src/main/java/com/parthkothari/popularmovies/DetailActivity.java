package com.parthkothari.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private String mTitle;
    private String mOverView;
    private double mAverageVote;
    private String mReleaseDate;
    private String mPosterPath;
    private String mBackdropPath;


    private TextView mDetailTitle;
    private TextView mDetailOverview;
    private TextView mDetailAverageVote;
    private TextView mDetailReleaseDate;
    private ImageView mDetailMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailTitle = findViewById(R.id.tv_detail_title);
        mDetailOverview = findViewById(R.id.tv_detail_overview);
        mDetailAverageVote = findViewById(R.id.tv_detail_average_vote);
        mDetailReleaseDate = findViewById(R.id.tv_detail_release_date);
        mDetailMoviePoster = findViewById(R.id.iv_detail_movie_poster);


        Intent originatorIntent = getIntent();
        Bundle movieDetails = originatorIntent.getExtras();

        mTitle = movieDetails.getString("title");
        mOverView = movieDetails.getString("overview");
        mAverageVote = movieDetails.getDouble("averageVote");
        mReleaseDate = movieDetails.getString("releaseDate");
        mPosterPath = movieDetails.getString("posterPath");
        mBackdropPath = movieDetails.getString("backdropPath");

        Log.d("DetailActivity", mPosterPath);

        mDetailTitle.setText(mTitle);
        mDetailOverview.setText(mOverView);
        mDetailReleaseDate.setText(mReleaseDate);
        mDetailAverageVote.setText(Double.toString(mAverageVote));

        Picasso.get()
                .load(mPosterPath)
                .into(mDetailMoviePoster);


    }
}
