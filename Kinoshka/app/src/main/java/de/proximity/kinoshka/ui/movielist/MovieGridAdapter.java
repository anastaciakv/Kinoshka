package de.proximity.kinoshka.ui.movielist;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.databinding.MovieItemBinding;
import de.proximity.kinoshka.entity.Movie;

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {


    private final android.databinding.DataBindingComponent bindingComponent;

    public interface MovieClickCallback {
        void onClick(Movie movie, View view);
    }

    final private MovieClickCallback callback;
    List<Movie> movies;

    public MovieGridAdapter(android.databinding.DataBindingComponent bindingComponent, MovieClickCallback callback) {
        this.bindingComponent = bindingComponent;
        this.callback = callback;

        movies = new ArrayList<>();
    }

    public void update(List<Movie> movieList) {
        movies.clear();
        movies.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MovieItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.movie_item, parent, false, bindingComponent);
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
        holder.bind(movies.get(position));
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