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
    private MovieCardClickListener mMovieCardClickListener;

    public RecyclerViewAdapter(ArrayList<Movie> dataset, MovieCardClickListener clickListener) {
        this.mMovieDataset = dataset;
        this.mMovieCardClickListener = clickListener;
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

    public interface MovieCardClickListener {
        void onMovieCardClick(int clickedItemIndex);
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mMovieTitle;
        private ImageView mMoviePoster;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            mMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mMovieCardClickListener.onMovieCardClick(clickedPosition);
        }
    }
}
