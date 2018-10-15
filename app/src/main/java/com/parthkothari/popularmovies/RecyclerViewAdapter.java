package com.parthkothari.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView v = (TextView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        viewHolder vh = new viewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        viewHolder.mTextView.setText(mMovieDataset.get(i).getmTitle());
    }

    @Override
    public int getItemCount() {
        return mMovieDataset.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }
}

