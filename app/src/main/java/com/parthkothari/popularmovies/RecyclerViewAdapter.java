package com.parthkothari.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {
    //    private String[] mDataset;
    private ArrayList<Movie> mMovieDataset;

    public RecyclerViewAdapter(ArrayList<Movie> mDataset) {
        this.mMovieDataset = mDataset;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LinearLayout v = (LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        viewHolder vh = new viewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        viewHolder.mMovieTitle.setText(mMovieDataset.get(i).getmTitle());
        Picasso.get()
                .load(mMovieDataset.get(i).getmPosterPath())
//                .error(R.drawable.no_image_available)
                .into(viewHolder.mMoviePoster);


    }

    @Override
    public int getItemCount() {
        return mMovieDataset.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private ImageView mMoviePoster;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        }
    }
}

