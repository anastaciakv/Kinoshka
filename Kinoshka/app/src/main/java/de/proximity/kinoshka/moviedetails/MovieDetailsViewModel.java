package de.proximity.kinoshka.moviedetails;


import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsViewModel extends ViewModel {

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Movie movie;
    private ObservableBoolean isBigPosterVisible;

    public MovieDetailsViewModel() {
        isBigPosterVisible = new ObservableBoolean(false);
    }

    public void onPosterClicked() {
        isBigPosterVisible.set(true);
    }

    public void onBigPosterClicked() {
        isBigPosterVisible.set(false);
    }

    public ObservableBoolean isBigPosterVisible() {
        return isBigPosterVisible;
    }

}
