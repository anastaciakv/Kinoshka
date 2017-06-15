package de.proximity.kinoshka.moviedetails;

import dagger.Module;
import dagger.Provides;
import de.proximity.kinoshka.entity.Movie;

@Module
public class MovieDetailsModule {
    private final MovieDetailsContract.View view;
    private final Movie movie;

    public MovieDetailsModule(MovieDetailsContract.View view, Movie movie) {
        this.view = view;
        this.movie = movie;
    }

    @Provides
    MovieDetailsContract.View provideMovieDetailsView() {
        return view;
    }

    @Provides
    MovieDetailsContract.Presenter providesMovieDetailsPresenter() {
        return new MovieDetailsPresenter(view, movie);
    }
}
