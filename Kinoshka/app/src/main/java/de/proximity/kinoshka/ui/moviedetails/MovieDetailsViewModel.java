package de.proximity.kinoshka.ui.moviedetails;


import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import javax.inject.Inject;

import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsViewModel extends ViewModel {

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Movie movie;
    public ObservableBoolean isBigPosterVisible;

    @Inject
    public MovieDetailsViewModel() {
        isBigPosterVisible = new ObservableBoolean(false);
    }

    public void onPosterClicked() {
        isBigPosterVisible.set(true);
    }

    public void onBigPosterClicked() {
        isBigPosterVisible.set(false);
    }
}