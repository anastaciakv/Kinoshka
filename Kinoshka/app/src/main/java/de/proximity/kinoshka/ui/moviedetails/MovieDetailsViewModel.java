package de.proximity.kinoshka.ui.moviedetails;


import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import de.proximity.kinoshka.entity.Movie;

public class MovieDetailsViewModel extends ViewModel {

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Movie movie;

    @Inject
    public MovieDetailsViewModel() {

    }

}