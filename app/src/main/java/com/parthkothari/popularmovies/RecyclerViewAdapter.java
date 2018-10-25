package com.parthkothari.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.viewHolder> {
    private ArrayList<Movie> mMovieDataset;
    private MovieCardClickListener mMovieCardClickListener;

    public RecyclerViewAdapter(ArrayList<Movie> dataset, MovieCardClickListener clickListener) {
        this.mMovieDataset = dataset;
        this.mMovieCardClickListener = clickListener;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        FrameLayout v = (FrameLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        Picasso.get()
                .load(mMovieDataset.get(i).getmPosterPath())
//                .error(R.drawable.no_image_available)
                .into(viewHolder.mMoviePoster);

    }

    @Override
    public int getItemCount() {
        return mMovieDataset.size();
    }

    public interface MovieCardClickListener {
        void onMovieCardClick(Movie clickedMovie);
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mMoviePoster;


        viewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mMovieCardClickListener.onMovieCardClick(mMovieDataset.get(clickedPosition));
        }
    }
}

