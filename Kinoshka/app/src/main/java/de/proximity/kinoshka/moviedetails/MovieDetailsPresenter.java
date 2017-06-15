package de.proximity.kinoshka.moviedetails;


import de.proximity.kinoshka.data.remote.NetworkModule;
import de.proximity.kinoshka.entity.Movie;

class MovieDetailsPresenter implements MovieDetailsContract.Presenter {
    private final MovieDetailsContract.View view;
    public final Movie movie;

    public MovieDetailsPresenter(MovieDetailsContract.View view, Movie movie) {
        this.view = view;
        this.movie = movie;
    }

    @Override
    public void start() {
        displayMovieData();
    }

    @Override
    public void onPosterClicked() {
        String imgUrl = NetworkModule.getImageUrl(NetworkModule.SupportedImageSize.w780, movie.posterPath);
        view.displayBigPoster(imgUrl);
    }

    @Override
    public void onBigPosterClicked() {
        view.hideBigPoster();
    }

    private void displayMovieData() {
        String posterUrl = NetworkModule.getImageUrl(NetworkModule.SupportedImageSize.w342, movie.posterPath);
        view.setMoviePoster(posterUrl);
        view.setMovieTitle(movie.originalTitle);
        view.setMovieDescription(movie.overview);
        view.setMovieReleaseDate(movie.releaseDate);
        view.setMovieRating(String.valueOf(movie.voteAverage));
    }
}
