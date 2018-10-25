package com.parthkothari.popularmovies;

class Movie {
    private String mTitle;
    private String mPosterPath;
    private double mAverageVote;
    private String mOverview;
    private String mReleaseDate;
    private String mBackdropPath;
    private String mPosterBaseUrl = "https://image.tmdb.org/t/p/w500";

    public Movie(String mTitle, String mPosterPath, int mId, double mAverageVote, String mOverview, String mReleaseDate, String mBackdropPath) {
        this.mTitle = mTitle;
        this.mPosterPath = mPosterPath;
        int mId1 = mId;
        this.mAverageVote = mAverageVote;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mBackdropPath = mBackdropPath;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPosterPath() {
        return mPosterBaseUrl + mPosterPath;
    }

    public double getmAverageVote() {
        return mAverageVote;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmBackdropPath() {
        return mPosterBaseUrl + mBackdropPath;
    }
}
