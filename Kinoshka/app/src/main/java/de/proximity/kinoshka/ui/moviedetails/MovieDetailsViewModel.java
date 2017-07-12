package de.proximity.kinoshka.ui.moviedetails;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.view.View;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.proximity.kinoshka.data.MovieTask;
import de.proximity.kinoshka.data.remote.ServerResponse;
import de.proximity.kinoshka.entity.Movie;
import de.proximity.kinoshka.entity.Review;
import de.proximity.kinoshka.entity.Trailer;

public class MovieDetailsViewModel extends ViewModel {
    private final MovieTask movieTask;
    public Movie movie;

    public MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    public MutableLiveData<List<Trailer>> trailers = new MutableLiveData<>();

    public ObservableBoolean isFavorite = new ObservableBoolean(false);
    public ObservableBoolean isTrailerAvailable = new ObservableBoolean(false);
    public ObservableBoolean isReviewAvailable = new ObservableBoolean(false);

    public void setMovie(Movie movie) {
        this.movie = movie;
        isFavorite.set(movieTask.checkIsFavorite(movie));
        fetchReviews();
        fetchTrailers();
    }

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public LiveData<List<Trailer>> getTrailers() {
        return trailers;
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
     //   if (movie.video)
            movieTask.fetchTrailers(movie.id, getTrailerCallback());
    }

    @Singleton
    private MovieTask.MovieTaskCallback<Review> getReviewCallback() {
        return new MovieTask.MovieTaskCallback<Review>() {


            @Override
            public void onSuccess(ServerResponse<Review> serverResponse) {
                reviews.setValue(serverResponse.items);
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
        return new MovieTask.MovieTaskCallback() {
            @Override
            public void onSuccess(ServerResponse serverResponse) {
                trailers.setValue(serverResponse.items);
                isTrailerAvailable.set(serverResponse.items != null && !serverResponse.items.isEmpty());
            }

            @Override
            public void onError() {
                isTrailerAvailable.set(false);
            }
        };
    }
}