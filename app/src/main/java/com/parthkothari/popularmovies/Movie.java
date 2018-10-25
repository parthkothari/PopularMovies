package com.parthkothari.popularmovies;

class Movie {
    private final String mTitle;
    private final String mPosterPath;
    private final double mAverageVote;
    private final String mOverview;
    private final String mReleaseDate;
    private final String mBackdropPath;
    private final String mPosterBaseUrl = "https://image.tmdb.org/t/p/w500";

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
