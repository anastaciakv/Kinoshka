package de.proximity.kinoshka.ui.movielist;


import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.binding.BindListAdapter;
import de.proximity.kinoshka.databinding.MovieItemBinding;
import de.proximity.kinoshka.entity.Movie;
import timber.log.Timber;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> implements BindListAdapter<Movie> {

    @Override
    public void replaceItems(List<Movie> items) {
        if (items == null) return;
        Timber.d("replaceItems, count " + items.size());
        movies.clear();
        movies.addAll(items);
        notifyDataSetChanged();
    }

    public interface MovieClickCallback {
        void onClick(Movie movie, View view);
    }

    final private MovieClickCallback callback;
    List<Movie> movies = new ArrayList<>();

    public MovieGridAdapter(MovieClickCallback callback) {
        this.callback = callback;
        movies = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final MovieItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.movie_item, parent, false);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie movie = binding.getMovie();
                if (movie != null && callback != null) {
                    callback.onClick(movie, binding.ivPoster);
                }
            }
        });
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
        ViewCompat.setTransitionName(holder.binding.ivPoster, String.valueOf(movies.get(position).id));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return movies.size();
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