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

public class MovieDetailsViewModel extends ViewModel {
    private final MovieTask movieTask;
    public ObservableBoolean isFavorite = new ObservableBoolean(false);

    public LiveData<List<Review>> getReviews() {
        return reviews;
    }

    public MutableLiveData<List<Review>> reviews = new MutableLiveData<>();
    public ObservableBoolean isReviewAvailable = new ObservableBoolean(false);

    public void setMovie(Movie movie) {
        this.movie = movie;
        isFavorite.set(movieTask.checkIsFavorite(movie));
        fetchReviews();
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

    public Movie movie;

    @Inject
    public MovieDetailsViewModel(MovieTask movieTask) {
        this.movieTask = movieTask;
    }

}