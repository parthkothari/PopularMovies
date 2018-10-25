package com.parthkothari.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mDetailTitle = findViewById(R.id.tv_detail_title);
        TextView mDetailOverview = findViewById(R.id.tv_detail_overview);
        TextView mDetailAverageVote = findViewById(R.id.tv_detail_average_vote);
        TextView mDetailReleaseDate = findViewById(R.id.tv_detail_release_date);
        ImageView mDetailMoviePoster = findViewById(R.id.iv_detail_movie_poster);

        Intent originatorIntent = getIntent();
        Bundle movieDetails = originatorIntent.getExtras();

        String mTitle = movieDetails.getString("title");
        String mOverView = movieDetails.getString("overview");
        double mAverageVote = movieDetails.getDouble("averageVote");
        String mReleaseDate = movieDetails.getString("releaseDate");
        String mPosterPath = movieDetails.getString("posterPath");
        String mBackdropPath = movieDetails.getString("backdropPath");

        mDetailTitle.setText(mTitle);
        mDetailOverview.setText(mOverView);
        mDetailReleaseDate.setText(mReleaseDate);
        mDetailAverageVote.setText(Double.toString(mAverageVote));

        Picasso.get()
                .load(mPosterPath)
                .into(mDetailMoviePoster);


    }
}
