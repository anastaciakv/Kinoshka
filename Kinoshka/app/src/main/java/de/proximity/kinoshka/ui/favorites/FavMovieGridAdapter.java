package de.proximity.kinoshka.ui.favorites;


import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.databinding.MovieItemBinding;
import de.proximity.kinoshka.entity.Movie;

public class FavMovieGridAdapter extends RecyclerView.Adapter<FavMovieGridAdapter.MovieViewHolder> {
    private Cursor cursor;
    final private MovieClickCallback callback;

    public FavMovieGridAdapter(MovieClickCallback callback) {
        this.callback = callback;
    }

    public interface MovieClickCallback {
        void onClick(Movie movie, View view);
    }

    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public void swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (cursor == c) {
            return; // bc nothing has changed
        }
        this.cursor = c; // new cursor value assigned
        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.movie_item, parent, false);
        binding.getRoot().setOnClickListener(v -> {
            Movie movie = binding.getMovie();
            if (movie != null && callback != null) {
                callback.onClick(movie, binding.ivPoster);
            }
        });
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        cursor.moveToPosition(position);
        Movie movie = new Movie(cursor);
        holder.bind(movie);
        ViewCompat.setTransitionName(holder.binding.ivPoster, String.valueOf(movie.id));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final MovieItemBinding binding;

        MovieViewHolder(MovieItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Movie movie) {
            binding.setMovie(movie);
        }
    }
}
