package com.parthkothari.popularmovies;

public class Movie {
    private String mTitle;
    private String mPosterPath;
    private int mId;
    private double mAverageVote;


    public Movie(String mTitle, String mPosterPath, int mId, double mAverageVote) {
        this.mTitle = mTitle;
        this.mPosterPath = mPosterPath;
        this.mId = mId;
        this.mAverageVote = mAverageVote;
    }

    public String getmTitle() {
        return mTitle;
    }
}
