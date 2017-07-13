package de.proximity.kinoshka.ui.moviedetails;


import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.ServerResponse;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.Review;
import de.proximity.kinoshka.entity.Trailer;

public class MovieDetailsViewModel extends BaseObservable {
    private final MovieTask movieTask;
    public Movie movie;

    public ObservableField<List<Review>> reviews = new ObservableField<>();
    public ObservableField<List<Trailer>> trailers = new ObservableField<>();

    public ObservableBoolean isFavorite = new ObservableBoolean(false);
    public ObservableBoolean isTrailerAvailable = new ObservableBoolean(false);
    public ObservableBoolean isReviewAvailable = new ObservableBoolean(false);

    public void setMovie(Movie movie) {
        if (movie.equals(this.movie)) return;
        this.movie = movie;
        isFavorite.set(movieTask.checkIsFavorite(movie));
        fetchReviews();
        fetchTrailers();
    }

    public void onFavoritesClicked(View v) {
        isFavorite.set(!isFavorite.get());
        if (isFavorite.get()) {
            movieTask.addToFavorites(movie);
        } else {
            movieTask.removeFromFavorites(movie);
        }
    }

    private void fetchReviews() {
        movieTask.fetchReviews(movie.id, getReviewCallback());
    }

    private void fetchTrailers() {
        movieTask.fetchTrailers(movie.id, getTrailerCallback());
    }

    @Singleton
    private MovieTask.MovieTaskCallback<Review> getReviewCallback() {
        return new MovieTask.MovieTaskCallback<Review>() {

            @Override
            public void onSuccess(ServerResponse<Review> serverResponse) {
                reviews.set(serverResponse.items);
                reviews.notifyChange();
                isReviewAvailable.set(serverResponse.items != null && !serverResponse.items.isEmpty());
            }

            @Override
            public void onError() {
                isReviewAvailable.set(false);
            }
        };
    }

    @Inject
    public MovieDetailsViewModel(MovieTask movieTask) {
        this.movieTask = movieTask;
    }

    @Singleton
    private MovieTask.MovieTaskCallback getTrailerCallback() {
        return new MovieTask.MovieTaskCallback<Trailer>() {

            @Override
            public void onSuccess(ServerResponse<Trailer> serverResponse) {
                trailers.set(serverResponse.items);
                trailers.notifyChange();
                isTrailerAvailable.set(serverResponse.items != null && !serverResponse.items.isEmpty());
            }

            @Override
            public void onError() {
                isTrailerAvailable.set(false);
            }
        };
    }
}